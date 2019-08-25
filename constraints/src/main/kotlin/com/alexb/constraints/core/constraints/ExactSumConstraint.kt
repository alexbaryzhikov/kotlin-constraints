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
import com.alexb.constraints.utils.*

/**
 * Constraint enforcing that values of given variables sum exactly
 * to a given amount.
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