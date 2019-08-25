import com.alexb.constraints.Problem
import com.alexb.constraints.core.constraints.ExactSumConstraint
import kotlin.system.measureTimeMillis

/*
100 coins must sum to $5.00

That's kind of a country-specific problem, since depending on the
country there are different values for coins. Here is presented
the solution for a given set.
*/

fun coins(): Pair<List<Map<String, Int>>, List<String>> {

    fun Double.round(n: Int): Double {
        var m = 1.0
        repeat(n) { m *= 10 }
        return kotlin.math.round(this * m) / m
    }

    val total = 5.0
    val variables = listOf("0.01", "0.05", "0.10", "0.50", "1.00")
    val values = variables.map { it.toDouble() }
    val problem = Problem<String, Int>().apply {
        for ((variable, value) in variables.zip(values)) {
            addVariable(variable, (0..(total / value).toInt()).toList())
        }
        addConstraint(ExactSumConstraint(100))
        addConstraint({ amounts ->
            var sum = 0.0
            for ((value, amount) in values.zip(amounts)) {
                sum += value * amount
                sum = sum.round(2)
            }
            sum == 5.0
        }, variables)
    }
    val solutions = problem.getSolutions()
    return Pair(solutions, variables)
}

fun main() {
    lateinit var result: Pair<List<Map<String, Int>>, List<String>>
    val t = measureTimeMillis { result = coins() }
    println("Found ${result.first.size} solution(s) in $t ms\n")
    val (solutions, variables) = result
    for ((i, solution) in solutions.withIndex()) {
        print(String.format("Solution %03d: ", i + 1))
        for (v in variables) {
            print("${v}x${solution[v]}, ")
        }
        println()
    }
}
