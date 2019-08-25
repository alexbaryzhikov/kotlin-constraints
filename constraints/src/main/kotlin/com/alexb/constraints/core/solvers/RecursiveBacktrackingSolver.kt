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
 * Recursive problem solver with backtracking capabilities.
 *
 * @param forwardcheck If false forward checking will not be requested
 *        to constraints while looking for solutions.
 */
class RecursiveBacktrackingSolver<V : Any, D : Any>(
    private val forwardcheck: Boolean = true
) : Solver<V, D> {

    override fun getSolution(
        domains: HashMap<V, Domain<D>>,
        constraints: ArrayList<ConstraintEnv<V, D>>,
        vconstraints: HashMap<V, ArrayList<ConstraintEnv<V, D>>>
    ): Map<V, D>? {
        val solutions =
            recursiveBacktracking(ArrayList(), domains, vconstraints, HashMap(), true)
        return if (solutions.isNotEmpty()) solutions[0] else null
    }

    override fun getSolutions(
        domains: HashMap<V, Domain<D>>,
        constraints: ArrayList<ConstraintEnv<V, D>>,
        vconstraints: HashMap<V, ArrayList<ConstraintEnv<V, D>>>
    ): List<Map<V, D>> {
        return recursiveBacktracking(ArrayList(), domains, vconstraints, HashMap(), false)
    }

    override fun getSolutionSequence(
        domains: HashMap<V, Domain<D>>,
        constraints: ArrayList<ConstraintEnv<V, D>>,
        vconstraints: HashMap<V, ArrayList<ConstraintEnv<V, D>>>
    ): Sequence<Map<V, D>> {
        throw UnsupportedOperationException()
    }

    private fun recursiveBacktracking(
        solutions: ArrayList<Map<V, D>>,
        domains: HashMap<V, Domain<D>>,
        vconstraints: HashMap<V, ArrayList<ConstraintEnv<V, D>>>,
        assignments: HashMap<V, D>,
        single: Boolean
    ): List<Map<V, D>> {
        val prioritizedVariables = ArrayList(domains.keys).apply {
            sortWith(compareBy({ -vconstraints[it]!!.size }, { domains[it]!!.size }))
        }
        var variable: V? = null
        for (v in prioritizedVariables) {
            if (v !in assignments) {
                variable = v
                break
            }
        }
        if (variable == null) {
            solutions.add(HashMap(assignments))
            return solutions
        }
        val pushDomains: ArrayList<Domain<D>>?
        if (forwardcheck) {
            pushDomains = ArrayList()
            for (x in domains.keys) {
                if (x !in assignments) {
                    pushDomains.add(domains[x]!!)
                }
            }
        } else {
            pushDomains = null
        }
        for (value in domains[variable]!!) {
            assignments[variable] = value
            pushDomains?.forEach { it.pushState() }
            var found = true
            for ((constraint, variables) in vconstraints[variable]!!) {
                if (!constraint(variables, domains, assignments, !pushDomains.isNullOrEmpty())) {
                    // Value is not good.
                    found = false
                    break
                }
            }
            if (found) {
                // Value is good. Recurse and get next variable.
                recursiveBacktracking(solutions, domains, vconstraints, assignments, single)
                if (solutions.isNotEmpty() && single) {
                    return solutions
                }
            }
            pushDomains?.forEach { it.popState() }
        }
        assignments.remove(variable)
        return solutions
    }
}
