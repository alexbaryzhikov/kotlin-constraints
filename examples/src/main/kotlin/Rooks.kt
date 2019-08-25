import com.alexb.constraints.Problem
import kotlin.system.measureTimeMillis

fun rooks(size: Int): List<Map<Int, Int>> {
    val cols = (0 until size).toList()
    val rows = (0 until size).toList()
    val problem = Problem<Int, Int>().apply {
        addVariables(cols, rows)
        for (col1 in cols) {
            for (col2 in cols) {
                if (col1 < col2) {
                    addConstraint({ row1, row2 -> row1 != row2 }, listOf(col1, col2))
                }
            }
        }
    }
    return problem.getSolutions()
}

fun main() {
    val size = 8
    lateinit var solutions: List<Map<Int, Int>>
    val t = measureTimeMillis { solutions = rooks(size) }
    println("Found ${solutions.size} solution(s) in $t ms\n")
    println(solutions.random())
}