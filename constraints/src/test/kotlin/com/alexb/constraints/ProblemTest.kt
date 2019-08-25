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

package com.alexb.constraints

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ProblemTest {

    @Test
    fun solutionsAbc() {
        val problem = Problem<String, Int>().apply {
            addVariables(listOf("a", "b", "c"), (1..9).toList())
        }
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

    @Test
    fun reset() {
        val problem = Problem<String, Int>().apply {
            addVariable("a", listOf(1, 2))
            reset()
        }
        assertThat(problem.getSolution()).isNull()
    }

    @Test
    fun addVariable() {
        val problem = Problem<String, Int>().apply {
            addVariable("a", listOf(1, 2))
        }
        assertThat(problem.getSolution()).isIn(listOf(mapOf("a" to 1), mapOf("a" to 2)))
    }

    @Test
    fun addVariables() {
        val problem = Problem<String, Int>().apply {
            addVariables(listOf("a", "b"), listOf(1, 2, 3))
        }
        assertThat(problem.getSolutions()).hasSize(9)
    }

    @Test
    fun addConstraint1() {
        val problem = Problem<String, Int>().apply {
            addVariables(listOf("a", "b"), listOf(1, 2, 3))
            addConstraint({ args -> args[1] == args[0] + 1 }, listOf("a", "b"))
        }
        assertThat(problem.getSolutions()).containsExactly(
            mapOf("a" to 1, "b" to 2),
            mapOf("a" to 2, "b" to 3)
        )
    }

    @Test
    fun addConstraint2() {
        val problem = Problem<String, Int>().apply {
            addVariables(listOf("a", "b", "c"), listOf(1, 2, 3))
            addConstraint({ a, b, c -> a + b == c }, listOf("a", "b", "c"))
        }
        assertThat(problem.getSolution()).isIn(
            listOf(
                mapOf("a" to 1, "b" to 1, "c" to 2),
                mapOf("a" to 1, "b" to 2, "c" to 3),
                mapOf("a" to 2, "b" to 1, "c" to 3)
            )
        )
    }

    @Test
    fun addConstraint3() {
        val problem = Problem<String, Int>().apply {
            addVariables(listOf("a", "b"), listOf(1, 2, 3))
            addConstraint({ a, b -> b == a + 1 }, listOf("a", "b"))
        }
        assertThat(problem.getSolutions()).containsExactly(
            mapOf("a" to 1, "b" to 2),
            mapOf("a" to 2, "b" to 3)
        )
    }

    @Test
    fun addConstraint4() {
        val problem = Problem<String, Int>().apply {
            addVariables(listOf("a", "b"), listOf(1, 2))
            addConstraint({ a -> a == 2 }, "a")
        }
        assertThat(problem.getSolutions()).containsExactly(
            mapOf("a" to 2, "b" to 1),
            mapOf("a" to 2, "b" to 2)
        )
    }

    @Test
    fun getSolution() {
        val problem = Problem<String, Int>()
        assertThat(problem.getSolution()).isNull()
        problem.addVariable("a", listOf(42))
        assertThat(problem.getSolution()).isEqualTo(mapOf("a" to 42))
    }

    @Test
    fun getSolutions() {
        val problem = Problem<String, Int>()
        assertThat(problem.getSolutions()).isEmpty()
        problem.addVariable("a", listOf(42))
        assertThat(problem.getSolutions()).containsExactly(mapOf("a" to 42))
    }

    @Test
    fun getSolutionSequence() {
        val problem = Problem<String, Int>()
        assertThat(problem.getSolutionSequence().toList()).isEmpty()
        problem.addVariable("a", listOf(42))
        assertThat(problem.getSolutionSequence().toList()).containsExactly(mapOf("a" to 42))
    }
}
