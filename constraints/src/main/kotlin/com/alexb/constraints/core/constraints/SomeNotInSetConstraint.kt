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

/**
 * Constraint enforcing that at least some of the values of given
 * variables must not be present in a given set.
 *
 * @param set Set of values to be checked.
 * @param n Minimum number of assigned values that should not be present
 *        in set (default is 1).
 * @param exact Whether the number of assigned values which are
 *        not present in set must be exactly n.
 */
class SomeNotInSetConstraint<V : Any, D : Number>(
    private val set: Set<D>,
    private val n: Int = 1,
    private val exact: Boolean = false
) : Constraint<V, D> {

    override fun invoke(
        variables: List<V>,
        domains: HashMap<V, Domain<D>>,
        assignments: HashMap<V, D>,
        forwardcheck: Boolean
    ): Boolean {
        var missing = 0
        var found = 0
        for (v in variables) {
            if (v in assignments) {
                if (assignments[v]!! !in set) {
                    found++
                }
            } else {
                missing++
            }
        }
        if (missing != 0) {
            if (exact) {
                if (n < found || n > missing + found) return false
            } else {
                if (n > missing + found) return false
            }
            if (forwardcheck && n == missing + found) {
                // All unassigned variables must be assigned to
                // values not in the set.
                for (v in variables) {
                    if (v !in assignments) {
                        val domain = domains[v]!!
                        for (value in ArrayList(domain)) {
                            if (value in set) {
                                domain.hideValue(value)
                            }
                        }
                        if (domain.isEmpty()) return false
                    }
                }
            }
        } else {
            if (exact) {
                if (n != found) return false
            } else {
                if (n > found) return false
            }
        }
        return true
    }
}