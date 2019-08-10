package com.alexb.constraints.core.constraints

import com.alexb.constraints.core.*
import com.alexb.constraints.core.constraints.MaxSumConstraint.Companion.DOUBLE_PRECISION
import com.alexb.constraints.core.constraints.MaxSumConstraint.Companion.FLOAT_PRECISION
import com.alexb.constraints.utils.*

/**
 * Constraint enforcing that values of given variables sum up to
 * a given amount. For [Float]s the precision is up to [FLOAT_PRECISION] decimals,
 * for [Double]s -- up to [DOUBLE_PRECISION].
 *
 * Example:
 * ```
 *     problem = Problem()
 *     problem.addVariables(listOf("a", "b"), listOf(1, 2))
 *     problem.addConstraint(MaxSumConstraint(3))
 *     problem.getSolutions()
 * ```
 * Output:
 * ```
 *     [{a=1, b=1}, {a=1, b=2}, {a=2, b=1}]
 * ```
 *
 * @param maxSum Value to be considered as the maximum sum.
 */
class MaxSumConstraint<V : Any, D : Number>(
    private val maxSum: D
) : Constraint<V, D> {

    override fun invoke(
        variables: List<V>,
        domains: HashMap<V, Domain<D>>,
        assignments: HashMap<V, D>,
        forwardcheck: Boolean
    ): Boolean {
        var sum: D = maxSum.zero() // get the right type of zero
        for (v in variables) {
            val value = assignments[v]
            if (value != null) {
                sum += value
                @Suppress("UNCHECKED_CAST")
                sum = when (sum) {
                    is Float -> sum.toDouble().round(FLOAT_PRECISION).toFloat() as D
                    is Double -> sum.round(DOUBLE_PRECISION) as D
                    else -> sum
                }
            }
        }
        if (sum > maxSum) {
            return false
        }
        if (forwardcheck) {
            for (v in variables) {
                if (v !in assignments) {
                    val domain = domains[v]!!
                    for (value in ArrayList(domain)) {
                        if (sum + value > maxSum) {
                            domain.hideValue(value)
                        }
                    }
                    if (domain.isEmpty()) {
                        return false
                    }
                }
            }
        }
        return true
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
                if (value > maxSum) {
                    domain.remove(value)
                }
            }
        }
    }

    override fun toString(): String = "MaxSumConstraint@$maxSum"

    companion object {
        const val FLOAT_PRECISION = 4
        const val DOUBLE_PRECISION = 10
    }
}
