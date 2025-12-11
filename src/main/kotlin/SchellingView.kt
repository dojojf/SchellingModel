import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.layout.GridPane
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import javafx.stage.Stage

class SchellingView(private val model: SchellingModel) : Application() {
    override fun start(stage: Stage) {
        val gridPane = GridPane()
        val grid = model.getGrid()
        for (row in grid.indices) {
            for (col in grid[row].indices) {
                val cell = grid[row][col]
                val rect = Rectangle(10.0, 10.0)
                rect.fill = when (cell.community) {
                    0 -> Color.RED
                    1 -> Color.BLUE
                    else -> Color.WHITE
                }
                gridPane.add(rect, col, row)
            }
        }
        stage.scene = Scene(gridPane)
        stage.title = "Schelling Model"
        stage.show()
    }
}
