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
 * Constraint which wraps a function defining the constraint logic.
 *
 * @param func Function wrapped and queried for constraint logic.
 */
class BiFunctionConstraint<V : Any, D : Any>(
    private val func: (D, D) -> Boolean
) : Constraint<V, D> {

    override fun invoke(
        variables: List<V>,
        domains: HashMap<V, Domain<D>>,
        assignments: HashMap<V, D>,
        forwardcheck: Boolean
    ): Boolean {
        if (variables.size != 2) {
            throw IllegalArgumentException("Wrong number of arguments")
        }
        val args = variables.map { assignments[it] }
        val missing = args.count { it == null }
        return if (missing != 0) {
            !forwardcheck || missing != 1 || forwardCheck(variables, domains, assignments)
        } else {
            func(args[0]!!, args[1]!!)
        }
    }
}