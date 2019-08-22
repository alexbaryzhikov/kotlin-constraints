package com.alexb.constraints.core.solvers

import com.alexb.constraints.Problem
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class SolversTest {

    @Test
    fun backtrackingSolver() {
        val result = listOf(
            mapOf("a" to 1, "b" to 2),
            mapOf("a" to 1, "b" to 3),
            mapOf("a" to 2, "b" to 3)
        )

        val problem = Problem<String, Int>(BacktrackingSolver()).apply {
            addVariables(listOf("a", "b"), listOf(1, 2, 3))
            addConstraint({ a, b -> b > a })
        }

        for (solution in problem.getSolutionSequence()) {
            assertThat(solution).isIn(result)
        }
        assertThat(problem.getSolution()).isIn(result)
        assertThat(problem.getSolutions()).containsExactlyElementsIn(result)
    }

    @Test(expected = UnsupportedOperationException::class)
    fun recursiveBacktrackingSolver() {
        val result = listOf(
            mapOf("a" to 1, "b" to 2),
            mapOf("a" to 1, "b" to 3),
            mapOf("a" to 2, "b" to 3)
        )

        val problem = Problem<String, Int>(RecursiveBacktrackingSolver()).apply {
            addVariables(listOf("a", "b"), listOf(1, 2, 3))
            addConstraint({ a, b -> b > a })
        }

        assertThat(problem.getSolution()).isIn(result)
        assertThat(problem.getSolutions()).containsExactlyElementsIn(result)
        problem.getSolutionSequence()
    }
}
