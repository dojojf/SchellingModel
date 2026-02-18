import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import javafx.stage.Stage

class SchellingView() : Application() {
    private val size = 20
    private val model = SchellingModel(size = size, similarityThreshold = 0.3)
    private val cellSize = 20.0
    private lateinit var gridPane: Pane
    private lateinit var iterationsLabel: Label
    private lateinit var groupsLabel: Label

    override fun start(stage: Stage) {
        stage.title = "Schelling Segregation Model"

        // Création des composants UI
        val runButton = Button("Run Simulation").apply {
            setOnAction { runSimulation() }
        }

        iterationsLabel = Label("Iterations: 0")
        groupsLabel = Label("Groups: 0")

        val topBox = HBox(10.0, runButton, iterationsLabel, groupsLabel)

        gridPane = Pane().apply {
            prefWidth = model.getCopyOfGrid().size * cellSize
            prefHeight = model.getCopyOfGrid().size * cellSize
        }

        // Mise en page
        val root = BorderPane().apply {
            top = topBox
            center = gridPane
        }

        stage.scene = Scene(root, cellSize * size, cellSize * size)
        stage.show()

        // Dessin de la grille initiale
        drawGrid()
    }

    private fun runSimulation() {
        val iterations = model.iterate(1000)
        iterationsLabel.text = "Iterations: $iterations"
        val groups = model.countGroups()
        groupsLabel.text = "Groups: $groups"
        drawGrid()
    }

    private fun drawGrid() {
        gridPane.children.clear()
        val grid = model.getCopyOfGrid()
        for (i in grid.indices) {
            for (j in grid[i].indices) {
                val cell = Rectangle(
                    j * cellSize,
                    i * cellSize,
                    cellSize,
                    cellSize
                ).apply {
                    fill = when (grid[i][j]) {
                        0 -> Color.WHITE
                        1 -> Color.BLUE
                        2 -> Color.RED
                        else -> Color.BLACK
                    }
                    stroke = Color.BLACK
                }
                gridPane.children.add(cell)
            }
        }
    }
}
