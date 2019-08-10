package com.alexb.constraints.core.constraints

import com.alexb.constraints.core.Constraint
import com.alexb.constraints.core.Domain

/**
 * Constraint enforcing that values of all given variables are different.
 *
 * Example:
 * ```
 *     problem = Problem()
 *     problem.addVariables(listOf("a", "b"), listOf(1, 2))
 *     problem.addConstraint(AllDifferentConstraint())
 *     problem.getSolutions()
 * ```
 * Output:
 * ```
 *     [{a=1, b=2}, {a=2, b=1}]
 * ```
 */
class AllDifferentConstraint<V : Any, D : Any> : Constraint<V, D> {

    override fun invoke(
        variables: List<V>,
        domains: HashMap<V, Domain<D>>,
        assignments: HashMap<V, D>,
        forwardcheck: Boolean
    ): Boolean {
        val seen = HashSet<D>()
        for (v in variables) {
            val value = assignments[v] ?: continue
            if (value in seen) {
                return false
            }
            seen.add(value)
        }
        if (forwardcheck) {
            for (v in variables) {
                if (v !in assignments) {
                    val domain = domains[v]!!
                    for (value in seen) {
                        if (value in domain) {
                            domain.hideValue(value)
                            if (domain.isEmpty()) {
                                return false
                            }
                        }
                    }
                }
            }
        }
        return true
    }

    override fun toString(): String = "AllDifferentConstraint"
}