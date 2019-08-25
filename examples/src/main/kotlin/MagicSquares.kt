import com.alexb.constraints.Problem
import com.alexb.constraints.core.constraints.AllDifferentConstraint
import com.alexb.constraints.core.constraints.ExactSumConstraint
import kotlin.system.measureTimeMillis

fun magicSquares(): List<Map<Int, Int>> {
    val problem = Problem<Int, Int>().apply {
        addVariables((0 until 16).toList(), (1 until 17).toList())
        addConstraint(AllDifferentConstraint(), (0 until 16).toList())
        addConstraint(ExactSumConstraint(34), listOf(0, 5, 10, 15))
        addConstraint(ExactSumConstraint(34), listOf(3, 6, 9, 12))
        for (row in 0 until 4) {
            addConstraint(ExactSumConstraint(34), List(4) { i -> row * 4 + i })
        }
        for (col in 0 until 4) {
            addConstraint(ExactSumConstraint(34), List(4) { i -> col + 4 * i })
        }
    }
    return problem.getSolutions()
}

fun main() {
    lateinit var solutions: List<Map<Int, Int>>
    val t = measureTimeMillis { solutions = magicSquares() }
    println("Found ${solutions.size} solution(s) in $t ms\n")
    println(solutions.random())
}