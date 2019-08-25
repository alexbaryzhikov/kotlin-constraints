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

package com.alexb.constraints.core.constraints

import com.alexb.constraints.core.Constraint
import com.alexb.constraints.core.Domain
import com.alexb.constraints.utils.compareTo
import com.alexb.constraints.utils.plus
import com.alexb.constraints.utils.round
import com.alexb.constraints.utils.zero

/**
 * Constraint enforcing that values of given variables sum at least
 * to a given amount. For [Float]s the precision is up to [FLOAT_PRECISION] decimals,
 * for [Double]s -- up to [DOUBLE_PRECISION].
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
