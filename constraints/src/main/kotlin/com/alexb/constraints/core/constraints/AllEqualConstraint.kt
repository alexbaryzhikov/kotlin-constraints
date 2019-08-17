package com.alexb.constraints.core.constraints

import com.alexb.constraints.core.Constraint
import com.alexb.constraints.core.Domain

/**
 * Constraint enforcing that values of all given variables are equal.
 */
class AllEqualConstraint<V : Any, D : Any> : Constraint<V, D> {

    override fun invoke(
        variables: List<V>,
        domains: HashMap<V, Domain<D>>,
        assignments: HashMap<V, D>,
        forwardcheck: Boolean
    ): Boolean {
        var singleValue: D? = null
        for (v in variables) {
            val value = assignments[v]
            if (singleValue == null) {
                singleValue = value
            } else if (value != null && value != singleValue) {
                return false
            }
        }
        if (forwardcheck && singleValue != null) {
            for (v in variables) {
                if (v !in assignments) {
                    val domain = domains[v]!!
                    if (singleValue !in domain) {
                        return false
                    }
                    for (value in ArrayList(domain)) {
                        if (value != singleValue) {
                            domain.hideValue(value)
                        }
                    }
                }
            }
        }
        return true
    }

    override fun toString(): String = "AllEqualConstraint"
}