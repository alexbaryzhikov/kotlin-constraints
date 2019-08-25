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