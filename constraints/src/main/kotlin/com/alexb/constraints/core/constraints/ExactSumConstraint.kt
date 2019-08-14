package com.alexb.constraints.core.constraints

import com.alexb.constraints.core.Constraint
import com.alexb.constraints.core.Domain
import com.alexb.constraints.utils.*

/**
 * Constraint enforcing that values of given variables sum exactly
 * to a given amount.
 *
 * Example:
 * ```
 *     val problem = Problem().apply {
 *         addVariables(listOf("a", "b"), listOf(1, 2))
 *         addConstraint(ExactSumConstraint(3))
 *     }
 *     problem.getSolutions()
 * ```
 * Result:
 * ```
 *     [{a=1, b=2}, {a=2, b=1}]
 * ```
 *
 * @param exactSum Value to be considered as the exact sum.
 */
class ExactSumConstraint<V : Any, D : Number>(
    private val exactSum: D
) : Constraint<V, D> {

    override fun invoke(
        variables: List<V>,
        domains: HashMap<V, Domain<D>>,
        assignments: HashMap<V, D>,
        forwardcheck: Boolean
    ): Boolean {
        var sum: D = exactSum.zero()
        var missing = false
        for (v in variables) {
            if (v in assignments) {
                sum += assignments[v]!!
                @Suppress("UNCHECKED_CAST")
                sum = when (sum) {
                    is Float -> sum.toDouble().round(FLOAT_PRECISION).toFloat() as D
                    is Double -> sum.round(DOUBLE_PRECISION) as D
                    else -> sum
                }
            } else {
                missing = true
            }
        }
        if (sum > exactSum) {
            return false
        }
        if (forwardcheck and missing) {
            for (v in variables) {
                if (v !in assignments) {
                    val domain = domains[v]!!
                    for (value in ArrayList(domain)) {
                        if (sum + value > exactSum) {
                            domain.hideValue(value)
                        }
                    }
                    if (domain.isEmpty()) {
                        return false
                    }
                }
            }
        }
        return if (missing) sum <= exactSum else sum == exactSum
    }

    override fun preprocess(
        variables: List<V>,
        domains: HashMap<V, Domain<D>>,
        constraints: ArrayList<ConstraintEnv<V, D>>,
        vconstraints: HashMap<V, ArrayList<ConstraintEnv<V, D>>>
    ) {
        super.preprocess(variables, domains, constraints, vconstraints)
        for (v in variables) {
            val domain = domains[v]!!
            for (value in ArrayList(domain)) {
                if (value > exactSum) {
                    domain.remove(value)
                }
            }
        }
    }

    override fun toString(): String = "ExactSumConstraint@$exactSum"

    companion object {
        const val FLOAT_PRECISION = 4
        const val DOUBLE_PRECISION = 10
    }
}