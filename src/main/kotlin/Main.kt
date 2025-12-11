fun main() {
    val model = SchellingModel(size = 20, emptyPercentage = 0.2, minSimilarity = 0.5)
    model.runUntilConvergence()
    println("Iterations: ${model.getIterations()}")
    println("Number of groups: ${model.countGroups()}")
    Application.launch(SchellingView::class.java, *arrayOf<String>())
}
