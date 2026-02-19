open class SchellingModel(private val size: Int, private val similarityThreshold: Double, private val freePercentage: Double = 0.2) {
    protected var grid: Array<IntArray>
    private val empty = 0
    private val communityA = 1
    private val communityB = 2
    private var freeCells: MutableList<Pair<Int, Int>> = mutableListOf()

    init {
        require(size > 0) { "Grid size must be positive" }
        require(similarityThreshold in 0.0..1.0) { "Similarity threshold must be between 0 and 1" }
        grid = Array(size) { IntArray(size) }
        initializeGrid()
    }

    private fun initializeGrid() {
        val totalCells = size * size
        val communitySize = (totalCells * (1 - freePercentage) / 2).toInt()
        val freeCellsCount = (totalCells * freePercentage).toInt()

        // Remplit la grille avec les communautés et les cases vides
        val cells = mutableListOf<Int>().apply {
            addAll(List(communitySize) { communityA })
            addAll(List(communitySize) { communityB })
            addAll(List(freeCellsCount) { empty })
        }.shuffled()

        for (i in 0 until size) {
            for (j in 0 until size) {
                grid[i][j] = cells[i * size + j]
                if (grid[i][j] == empty) {
                    freeCells.add(Pair(i, j))
                }
            }
        }
    }

    open fun isSatisfied(x: Int, y: Int): Boolean {
        val current = grid[x][y]
        if (current == empty) return true

        val neighbors = getNeighbors(x, y)
        val sameCommunity = neighbors.count { grid[it.first][it.second] == current }
        val totalNeighbors = neighbors.size

        return if (totalNeighbors == 0) true else sameCommunity.toDouble() / totalNeighbors >= similarityThreshold
    }

    private fun getNeighbors(x: Int, y: Int): List<Pair<Int, Int>> {
        val neighbors = mutableListOf<Pair<Int, Int>>()
        for (i in maxOf(x - 1, 0)..minOf(x + 1, size - 1)) {
            for (j in maxOf(y - 1, 0)..minOf(y + 1, size - 1)) {
                if (i != x || j != y) {
                    neighbors.add(Pair(i, j))
                }
            }
        }
        return neighbors
    }

    fun iterate(maxIterations: Int = 1000): Int {
        var iterations = 0
        var changed: Boolean

        do {
            changed = false
            for (i in 0 until size) {
                for (j in 0 until size) {
                    if (grid[i][j] != empty && !isSatisfied(i, j)) {
                        moveAgent(i, j)
                        changed = true
                    }
                }
            }
            iterations++
        } while (changed && iterations < maxIterations)

        return iterations
    }

    private fun moveAgent(x: Int, y: Int) {
        if (freeCells.isEmpty()) return

        val current = grid[x][y]
        val newPosition = freeCells.random()
        grid[x][y] = empty
        grid[newPosition.first][newPosition.second] = current
        freeCells.remove(newPosition)
        freeCells.add(Pair(x, y))
    }

    open fun getCopyOfGrid(): Array<IntArray> {
        return grid.copyOf()
    }

    open fun countGroups(): Int {
        // TODO
        return 0
    }

}
