package com.alexb.constraints.core.constraints

import com.alexb.constraints.Problem
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ConstraintsTest {

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
    fun triFunctionalConstraint() {
        val problem = Problem<String, Int>().apply {
            addVariables(listOf("a", "b", "c"), listOf(1, 2, 3))
            addConstraint({ a, b, c -> a + b == c }, listOf("a", "b", "c"))
        }
        val solutions = problem.getSolution()
        assertThat(solutions).isEqualTo(mapOf("a" to 1, "b" to 1, "c" to 2))
    }

    @Test
    fun functionalConstraint() {
        val problem = Problem<String, Int>().apply {
            addVariables(listOf("a", "b"), listOf(1, 2))
            addConstraint({ args -> args.any { it == 1 } })
        }
        val solutions = problem.getSolutions()
        assertThat(solutions).containsExactly(
            mapOf("a" to 1, "b" to 1),
            mapOf("a" to 1, "b" to 2),
            mapOf("a" to 2, "b" to 1)
        )
    }

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
    fun allEqualConstraint() {
        val problem = Problem<String, Int>()
        problem.addVariables(listOf("a", "b"), listOf(1, 2, 3))
        problem.addConstraint(AllEqualConstraint())
        val solutions = problem.getSolutions()
        assertThat(solutions).containsExactly(
            mapOf("a" to 1, "b" to 1),
            mapOf("a" to 2, "b" to 2),
            mapOf("a" to 3, "b" to 3)
        )
    }

    @Test
    fun maxSumConstraint() {
        val problem = Problem<String, Int>()
        problem.addVariables(listOf("a", "b"), listOf(1, 2, 3))
        problem.addConstraint(MaxSumConstraint(3))
        val solutions = problem.getSolutions()
        assertThat(solutions).containsExactly(
            mapOf("a" to 1, "b" to 1),
            mapOf("a" to 1, "b" to 2),
            mapOf("a" to 2, "b" to 1)
        )
    }

    @Test
    fun exactSumConstraint() {
        val problem = Problem<String, Int>()
        problem.addVariables(listOf("a", "b"), listOf(1, 2, 3))
        problem.addConstraint(ExactSumConstraint(3))
        val solutions = problem.getSolutions()
        assertThat(solutions).containsExactly(
            mapOf("a" to 1, "b" to 2),
            mapOf("a" to 2, "b" to 1)
        )
    }

    @Test
    fun minSumConstraint() {
        val problem = Problem<String, Double>()
        problem.addVariables(listOf("a", "b"), listOf(1.1, 2.5, 3.2))
        problem.addConstraint(MinSumConstraint(5.0))
        val solutions = problem.getSolutions()
        assertThat(solutions).containsExactly(
            mapOf("a" to 2.5, "b" to 2.5),
            mapOf("a" to 2.5, "b" to 3.2),
            mapOf("a" to 3.2, "b" to 2.5),
            mapOf("a" to 3.2, "b" to 3.2)
        )
    }

    @Test
    fun inSetConstraint() {
        val problem = Problem<String, Int>()
        problem.addVariables(listOf("a", "b"), listOf(1, 2, 3))
        problem.addConstraint(InSetConstraint(setOf(1)))
        val solutions = problem.getSolutions()
        assertThat(solutions).containsExactly(
            mapOf("a" to 1, "b" to 1)
        )
    }

    @Test
    fun notInSetConstraint() {
        val problem = Problem<String, Int>()
        problem.addVariables(listOf("a", "b"), listOf(1, 2, 3))
        problem.addConstraint(NotInSetConstraint(setOf(2)))
        val solutions = problem.getSolutions()
        assertThat(solutions).containsExactly(
            mapOf("a" to 1, "b" to 1),
            mapOf("a" to 1, "b" to 3),
            mapOf("a" to 3, "b" to 1),
            mapOf("a" to 3, "b" to 3)
        )
    }

    @Test
    fun someInSetConstraintNotExact() {
        val problem = Problem<String, Int>()
        problem.addVariables(listOf("a", "b", "c"), listOf(1, 2))
        problem.addConstraint(SomeInSetConstraint(setOf(2), 2))
        val solutions = problem.getSolutions()
        assertThat(solutions).containsExactly(
            mapOf("a" to 1, "b" to 2, "c" to 2),
            mapOf("a" to 2, "b" to 1, "c" to 2),
            mapOf("a" to 2, "b" to 2, "c" to 1),
            mapOf("a" to 2, "b" to 2, "c" to 2)
        )
    }

    @Test
    fun someInSetConstraintExact() {
        val problem = Problem<String, Int>()
        problem.addVariables(listOf("a", "b", "c"), listOf(1, 2))
        problem.addConstraint(SomeInSetConstraint(setOf(2), 2, true))
        val solutions = problem.getSolutions()
        assertThat(solutions).containsExactly(
            mapOf("a" to 1, "b" to 2, "c" to 2),
            mapOf("a" to 2, "b" to 1, "c" to 2),
            mapOf("a" to 2, "b" to 2, "c" to 1)
        )
    }

    @Test
    fun someNotInSetConstraintNotExact() {
        val problem = Problem<String, Int>()
        problem.addVariables(listOf("a", "b", "c"), listOf(1, 2))
        problem.addConstraint(SomeNotInSetConstraint(setOf(2), 2))
        val solutions = problem.getSolutions()
        assertThat(solutions).containsExactly(
            mapOf("a" to 1, "b" to 1, "c" to 1),
            mapOf("a" to 1, "b" to 1, "c" to 2),
            mapOf("a" to 1, "b" to 2, "c" to 1),
            mapOf("a" to 2, "b" to 1, "c" to 1)
        )
    }

    @Test
    fun someNotInSetConstraintExact() {
        val problem = Problem<String, Int>()
        problem.addVariables(listOf("a", "b", "c"), listOf(1, 2))
        problem.addConstraint(SomeNotInSetConstraint(setOf(2), 2, true))
        val solutions = problem.getSolutions()
        assertThat(solutions).containsExactly(
            mapOf("a" to 1, "b" to 1, "c" to 2),
            mapOf("a" to 1, "b" to 2, "c" to 1),
            mapOf("a" to 2, "b" to 1, "c" to 1)
        )
    }
}
