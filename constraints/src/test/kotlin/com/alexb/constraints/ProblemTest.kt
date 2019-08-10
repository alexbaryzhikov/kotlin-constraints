package com.alexb.constraints

import com.google.common.truth.Truth.assertThat
import com.alexb.constraints.core.constraints.AllDifferentConstraint
import org.junit.Test

class ProblemTest {

    @Test
    fun allDifferentConstraint() {
        val problem = Problem<String, Int>()
        problem.addVariables(listOf("a", "b"), listOf(1, 2, 3))
        problem.addConstraint(AllDifferentConstraint())
        val solutions = problem.getSolutions()
        assertThat(solutions).containsExactly(
            mapOf("a" to 1, "b" to 2),
            mapOf("a" to 1, "b" to 3),
            mapOf("a" to 2, "b" to 1),
            mapOf("a" to 2, "b" to 3),
            mapOf("a" to 3, "b" to 1),
            mapOf("a" to 3, "b" to 2)
        )
    }

    @Test
    fun triFunctionalConstraint() {
        val problem = Problem<String, Int>().apply {
            addVariables(listOf("a", "b", "c"), listOf(1, 2, 3))
            addConstraint({ a, b, c -> a + b == c }, listOf("a", "b", "c"))
        }
        val solutions = problem.getSolution()
        assertThat(solutions).isEqualTo(mapOf("a" to 1, "b" to 1, "c" to 2))
    }

    @Test
    fun biFunctionalConstraint() {
        val problem = Problem<String, Int>().apply {
            addVariables(listOf("a", "b"), listOf(1, 2, 3))
            addConstraint({ a, b -> b < a }, listOf("a", "b"))
        }
        val solutions = problem.getSolutions()
        assertThat(solutions).containsExactly(
            mapOf("a" to 2, "b" to 1),
            mapOf("a" to 3, "b" to 1),
            mapOf("a" to 3, "b" to 2)
        )
    }

    @Test
    fun uniFunctionalConstraint() {
        val problem = Problem<String, Int>().apply {
            addVariables(listOf("a", "b"), listOf(1, 2))
            addConstraint({ a -> a == 2 }, "a")
        }
        val solutions = problem.getSolutions()
        assertThat(solutions).containsExactly(
            mapOf("a" to 2, "b" to 1),
            mapOf("a" to 2, "b" to 2)
        )
    }

    @Test
    fun solutionsAbc() {
        val problem = Problem<String, Int>()
        problem.addVariables(listOf("a", "b", "c"), (1..9).toList())
        var minvalue = 999.0 / (9 * 3)
        var minSolution: Map<String, Int> = emptyMap()
        for (solution in problem.getSolutionSequence()) {
            val a = solution.getValue("a")
            val b = solution.getValue("b")
            val c = solution.getValue("c")
            val value = (a * 100 + b * 10 + c).toDouble() / (a + b + c)
            if (value < minvalue) {
                minvalue = value
                minSolution = solution
            }
        }
        assertThat(minSolution).containsExactlyEntriesIn(mapOf("a" to 1, "b" to 9, "c" to 9))
    }
}