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

package com.alexb.constraints.core.solvers

import com.alexb.constraints.core.Domain
import com.alexb.constraints.core.Solver
import com.alexb.constraints.utils.ConstraintEnv

/**
 * Problem solver based on the minimum conflicts theory.
 *
 * @param steps Maximum number of steps to perform before giving up
 * when looking for a solution (default is 1000).
 */
class MinConflictsSolver<V : Any, D : Any>(
    private val steps: Int = 1000
) : Solver<V, D> {

    override fun getSolution(
        domains: HashMap<V, Domain<D>>,
        constraints: ArrayList<ConstraintEnv<V, D>>,
        vconstraints: HashMap<V, ArrayList<ConstraintEnv<V, D>>>
    ): Map<V, D>? {
        val assignments = HashMap<V, D>()
        // Initial assignment.
        for (v in domains.keys) {
            assignments[v] = domains[v]!!.random()
        }
        repeat(steps) {
            var conflicted = false
            val vars = domains.keys.shuffled()
            for (v in vars) {
                // Check if variable is not in conflict.
                var foundConflict = false
                for ((constraint, variables) in vconstraints[v]!!) {
                    if (!constraint(variables, domains, assignments)) {
                        foundConflict = true
                        break
                    }
                }
                if (!foundConflict) continue
                // Variable has conflicts. Find values with less conflicts.
                var minCount = vconstraints[v]!!.size
                val minValues = ArrayList<D>()
                for (value in domains[v]!!) {
                    assignments[v] = value
                    var count = 0
                    for ((constraint, variables) in vconstraints[v]!!) {
                        if (!constraint(variables, domains, assignments)) {
                            count++
                        }
                    }
                    if (count == minCount) {
                        minValues.add(value)
                    } else if (count < minCount) {
                        minCount = count
                        minValues.clear()
                        minValues.add(value)
                    }
                }
                // Pick a random one from these values.
                assignments[v] = minValues.random()
                conflicted = true
            }
            if (!conflicted) return assignments
        }
        return null
    }

    override fun getSolutions(
        domains: HashMap<V, Domain<D>>,
        constraints: ArrayList<ConstraintEnv<V, D>>,
        vconstraints: HashMap<V, ArrayList<ConstraintEnv<V, D>>>
    ): List<Map<V, D>> {
        throw UnsupportedOperationException()
    }

    override fun getSolutionSequence(
        domains: HashMap<V, Domain<D>>,
        constraints: ArrayList<ConstraintEnv<V, D>>,
        vconstraints: HashMap<V, ArrayList<ConstraintEnv<V, D>>>
    ): Sequence<Map<V, D>> {
        throw UnsupportedOperationException()
    }
}
