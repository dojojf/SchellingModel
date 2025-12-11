import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach

class SchellingModelTest {
    private lateinit var model: SchellingModel

    @BeforeEach
    fun setUp() {
        // Initialise avec une petite grille pour les tests
        model = SchellingModel(size = 5, similarityThreshold = 0.5, freePercentage = 0.2)
    }

    @Test
    fun `test grid initialization`() {
        val grid = model.getGrid()
        assertEquals(5, grid.size)
        // Vérifie que le nombre de cases vides est environ 20%
        val emptyCells = grid.sumOf { row -> row.count { it == 0 } }
        assertTrue(emptyCells in 1..6) // Flexibilité pour l'aléatoire
    }

    @Test
    fun `test satisfaction with all neighbors same`() {
        // Simule une grille où tous les voisins sont identiques
        val mockGrid = Array(5) { IntArray(5) }
        for (i in 0 until 5) {
            for (j in 0 until 5) {
                mockGrid[i][j] = 1 // Toutes les cases sont de la communauté A
            }
        }
        // Remplace la grille pour ce test
        model = object : SchellingModel(5, 0.5, 0.0) {
            override fun getGrid(): Array<IntArray> = mockGrid
        }
        // Toute cellule doit être satisfaite
        assertTrue(model.isSatisfied(2, 2))
    }

    @Test
    fun `test satisfaction with no neighbors same`() {
        // Simule une grille où aucun voisin n'est identique
        val mockGrid = Array(5) { IntArray(5) }
        for (i in 0 until 5) {
            for (j in 0 until 5) {
                mockGrid[i][j] = if (i == 2 && j == 2) 1 else 2 // Centre communauté A, autres B
            }
        }
        // Remplace la grille pour ce test
        model = object : SchellingModel(5, 0.5, 0.0) {
            override fun getGrid(): Array<IntArray> = mockGrid
            fun isSatisfied(x: Int, y: Int): Boolean = super.isSatisfied(x, y)
        }
        // La cellule centrale ne doit pas être satisfaite
        assertFalse(model.isSatisfied(2, 2))
    }

    @Test
    fun `test iteration count`() {
        // Exécute les itérations et vérifie qu'elles s'arrêtent dans la limite
        val iterations = model.iterate(100)
        assertTrue(iterations <= 100)
    }

    @Test
    fun `test group counting`() {
        // Simule une grille avec deux groupes distincts
        val mockGrid = Array(5) { IntArray(5) }
        for (i in 0 until 3) {
            for (j in 0 until 3) {
                mockGrid[i][j] = 1 // Groupe en haut à gauche
            }
        }
        for (i in 3 until 5) {
            for (j in 3 until 5) {
                mockGrid[i][j] = 2 // Groupe en bas à droite
            }
        }
        // Remplace la grille pour ce test
        model = object : SchellingModel(5, 0.5, 0.0) {
            override fun getGrid(): Array<IntArray> = mockGrid
            override fun countGroups(): Int = super.countGroups()
        }
        // Doit trouver 2 groupes
        assertEquals(2, model.countGroups())
    }
}
