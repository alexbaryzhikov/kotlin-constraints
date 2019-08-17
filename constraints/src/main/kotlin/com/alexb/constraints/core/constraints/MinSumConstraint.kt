package com.alexb.constraints.core.constraints

import com.alexb.constraints.core.Constraint
import com.alexb.constraints.core.Domain
import com.alexb.constraints.utils.compareTo
import com.alexb.constraints.utils.plus
import com.alexb.constraints.utils.round
import com.alexb.constraints.utils.zero

/**
 * Constraint enforcing that values of given variables sum at least
 * to a given amount
 *
 * Example:
 * ```
 *     val problem = Problem().apply {
 *         addVariables(listOf("a", "b"), listOf(1, 2))
 *         addConstraint(MinSumConstraint(3))
 *     }
 *     problem.getSolutions()
 * ```
 * Result:
 * ```
 *     [{a=1, b=2}, {a=2, b=1}, {a=2, b=2}]
 * ```
 *
 * @param minSum Value to be considered as the minimum sum.
 */
class MinSumConstraint<V : Any, D : Number>(
    private val minSum: D
) : Constraint<V, D> {

    override fun invoke(
        variables: List<V>,
        domains: HashMap<V, Domain<D>>,
        assignments: HashMap<V, D>,
        forwardcheck: Boolean
    ): Boolean {
        for (v in variables) {
            if (v !in assignments) {
                return true
            }
        }
        var sum: D = minSum.zero()
        for (v in variables) {
            sum += assignments[v]!!
            @Suppress("UNCHECKED_CAST")
            sum = when (sum) {
                is Float -> sum.toDouble().round(FLOAT_PRECISION).toFloat() as D
                is Double -> sum.round(DOUBLE_PRECISION) as D
                else -> sum
            }
        }
        return sum >= minSum
    }

    override fun toString(): String = "MinSumConstraint@$minSum"

    companion object {
        const val FLOAT_PRECISION = 4
        const val DOUBLE_PRECISION = 10
    }
}