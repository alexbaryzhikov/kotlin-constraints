import com.alexb.constraints.Problem
import kotlin.math.abs
import kotlin.system.measureTimeMillis

/*
The eight queens puzzle is the problem of placing eight chess queens on an 8×8 chessboard
so that no two queens threaten each other; thus, a solution requires that no two queens
share the same row, column, or diagonal.
*/

fun queens(size: Int): List<Map<Int, Int>> {
    val problem = Problem<Int, Int>().apply {
        addVariables((0 until size).toList(), (0 until size).toList())
        for (rowA in 0 until size - 1) {
            for (rowB in rowA + 1 until size) {
                addConstraint(
                    { colA, colB -> colA != colB && abs(rowB - rowA) != abs(colB - colA) },
                    listOf(rowA, rowB)
                )
            }
        }
    }
    return problem.getSolutions()
}

fun main() {
    val size = 8
    lateinit var solutions: List<Map<Int, Int>>
    val t = measureTimeMillis { solutions = queens(size) }
    println("Found ${solutions.size} solution(s) in $t ms\n")
    for ((i, solution) in solutions.withIndex()) {
        print("Solution ${i + 1}: ")
        for (j in 0 until size) {
            print("${solution.getValue(j) + 1} ")
        }
        println()
    }
}
