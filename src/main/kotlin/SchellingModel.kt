import kotlin.math.sqrt
import kotlin.random.Random

data class Cell(val row: Int, val col: Int, var community: Int?)

class SchellingModel(
    private val size: Int,
    private val emptyPercentage: Double = 0.2,
    private val minSimilarity: Double = 0.5,
    private val maxIterations: Int = 1000,
    private val randomSeed: Long = System.currentTimeMillis()
) {
    private val grid: Array<Array<Cell>>
    private val random = Random(randomSeed)
    private var iterations = 0

    init {
        require(size > 0) { "Size must be positive" }
        require(emptyPercentage in 0.0..1.0) { "Empty percentage must be between 0 and 1" }
        grid = Array(size) { row ->
            Array(size) { col ->
                Cell(row, col, null)
            }
        }
        initializeGrid()
    }

    private fun initializeGrid() {
        val totalCells = size * size
        val emptyCells = (totalCells * emptyPercentage).toInt()
        val communitySize = (totalCells - emptyCells) / 2

        // Remplir avec deux communautés
        repeat(communitySize) {
            placeRandomCell(0)
            placeRandomCell(1)
        }
    }

    private fun placeRandomCell(community: Int) {
        while (true) {
            val row = random.nextInt(size)
            val col = random.nextInt(size)
            if (grid[row][col].community == null) {
                grid[row][col].community = community
                break
            }
        }
    }

    fun iterate(): Boolean {
        iterations++
        var moved = false
        for (row in 0 until size) {
            for (col in 0 until size) {
                val cell = grid[row][col]
                if (cell.community != null && !isSatisfied(cell)) {
                    val emptyCells = getEmptyCells()
                    if (emptyCells.isNotEmpty()) {
                        val newCell = emptyCells.random(random)
                        grid[newCell.row][newCell.col].community = cell.community
                        cell.community = null
                        moved = true
                    }
                }
            }
        }
        return moved
    }

    private fun isSatisfied(cell: Cell): Boolean {
        val neighbors = getNeighbors(cell)
        if (neighbors.isEmpty()) return true
        val sameCommunity = neighbors.count { it.community == cell.community }
        return sameCommunity.toDouble() / neighbors.size >= minSimilarity
    }

    private fun getNeighbors(cell: Cell): List<Cell> {
        val neighbors = mutableListOf<Cell>()
        for (dr in -1..1) {
            for (dc in -1..1) {
                if (dr == 0 && dc == 0) continue
                if (dr != 0 && dc != 0) continue // Orthogonal only
                val nr = cell.row + dr
                val nc = cell.col + dc
                if (nr in 0 until size && nc in 0 until size) {
                    grid[nr][nc].community?.let { neighbors.add(grid[nr][nc]) }
                }
            }
        }
        return neighbors
    }

    private fun getEmptyCells(): List<Cell> {
        return grid.flatMap { it.asList() }.filter { it.community == null }
    }

    fun runUntilConvergence(): Int {
        while (iterations < maxIterations) {
            if (!iterate()) break
        }
        return iterations
    }

    fun countGroups(): Int {
        val visited = Array(size) { BooleanArray(size) }
        var groupCount = 0
        for (row in 0 until size) {
            for (col in 0 until size) {
                if (grid[row][col].community != null && !visited[row][col]) {
                    visitGroup(grid[row][col], visited)
                    groupCount++
                }
            }
        }
        return groupCount
    }

    private fun visitGroup(cell: Cell, visited: Array<BooleanArray>) {
        val queue = mutableListOf(cell)
        visited[cell.row][cell.col] = true
        while (queue.isNotEmpty()) {
            val current = queue.removeAt(0)
            for (neighbor in getNeighbors(current)) {
                if (neighbor.community == cell.community && !visited[neighbor.row][neighbor.col]) {
                    visited[neighbor.row][neighbor.col] = true
                    queue.add(neighbor)
                }
            }
        }
    }

    fun getGrid(): Array<Array<Cell>> = grid.copyOf()
    fun getIterations(): Int = iterations
}
