import com.alexb.constraints.Problem

/*
What's the minimum value for:

       ABC
     -------
      A+B+C

From http://www.umassd.edu/mathcontest/abc.cfm
*/

fun abc(): Pair<Double, Map<String, Int>> {
    val problem = Problem<String, Int>().apply {
        addVariables(listOf("a", "b", "c"), (1..9).toList())
    }
    var minValue = 999.0 / (9 * 3)
    var minSolution: Map<String, Int> = emptyMap()
    for (solution in problem.getSolutionSequence()) {
        val a = solution.getValue("a")
        val b = solution.getValue("b")
        val c = solution.getValue("c")
        val value = (a * 100 + b * 10 + c).toDouble() / (a + b + c)
        if (value < minValue) {
            minValue = value
            minSolution = solution
        }
    }
    return Pair(minValue, minSolution)
}

fun main() {
    val (minValue, minSolution) = abc()
    println(minValue)
    println(minSolution)
}
