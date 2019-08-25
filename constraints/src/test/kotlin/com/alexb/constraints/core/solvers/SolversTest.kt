/*
Copyright 2019 Alex Baryzhikov

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

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

    @Test
    fun minConflictsSolver() {
        val result = listOf(
            mapOf("a" to 1, "b" to 2),
            mapOf("a" to 1, "b" to 3),
            mapOf("a" to 2, "b" to 3)
        )

        val problem = Problem<String, Int>(MinConflictsSolver()).apply {
            addVariables(listOf("a", "b"), listOf(1, 2, 3))
            addConstraint({ a, b -> b > a })
        }

        assertThat(problem.getSolution()).isIn(result)
    }
}
