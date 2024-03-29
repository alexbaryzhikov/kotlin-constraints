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

import com.alexb.constraints.utils.ConstraintEnv
import com.alexb.constraints.core.Domain
import com.alexb.constraints.core.Solver
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

/**
 * Problem solver with backtracking capabilities.
 *
 * @param forwardcheck If false forward checking will not be requested
 *        to constraints while looking for solutions.
 */
class BacktrackingSolver<V : Any, D : Any>(
    private val forwardcheck: Boolean = true
) : Solver<V, D> {

    override fun getSolution(
        domains: HashMap<V, Domain<D>>,
        constraints: ArrayList<ConstraintEnv<V, D>>,
        vconstraints: HashMap<V, ArrayList<ConstraintEnv<V, D>>>
    ): Map<V, D>? {
        val solutions = getSolutionSequence(domains, constraints, vconstraints)
        return solutions.first()
    }

    override fun getSolutions(
        domains: HashMap<V, Domain<D>>,
        constraints: ArrayList<ConstraintEnv<V, D>>,
        vconstraints: HashMap<V, ArrayList<ConstraintEnv<V, D>>>
    ): List<Map<V, D>> {
        return getSolutionSequence(domains, constraints, vconstraints).toList()
    }

    override fun getSolutionSequence(
        domains: HashMap<V, Domain<D>>,
        constraints: ArrayList<ConstraintEnv<V, D>>,
        vconstraints: HashMap<V, ArrayList<ConstraintEnv<V, D>>>
    ): Sequence<Map<V, D>> = sequence {

        val assignments: HashMap<V, D> = HashMap()
        val stack: Deque<Element<V, D>> = ArrayDeque()

        while (true) {
            lateinit var variable: V
            lateinit var values: ArrayList<D>
            var pushDomains: ArrayList<Domain<D>>? = null

            var found = false
            // Mix the Degree and Minimum Remaining Values (MRV) heuristics.
            val prioritizedVariables = ArrayList(domains.keys).apply {
                sortWith(compareBy({ -vconstraints[it]!!.size }, { domains[it]!!.size }))
            }
            for (v in prioritizedVariables) {
                if (v !in assignments) {
                    // Found unassigned variable.
                    variable = v
                    values = ArrayList(domains[v]!!)
                    values.reverse()
                    if (forwardcheck) {
                        pushDomains = ArrayList()
                        for (x in domains.keys) {
                            if (x !in assignments && x != v) {
                                pushDomains.add(domains[x]!!)
                            }
                        }
                    }
                    found = true
                    break
                }
            }

            if (!found) {
                // No unassigned variables. We've got a solution. Go back
                // to last variable, if there's one.
                yield(HashMap(assignments))
                if (stack.isEmpty()) {
                    return@sequence
                }
                val element = stack.pop()
                variable = element.variable
                values = element.values
                pushDomains = element.pushDomains
                pushDomains?.forEach { it.popState() }
            }

            while (true) {
                // We have a variable. Do we have any values left?
                if (values.isEmpty()) {
                    // No. Go back to last variable, if there's one.
                    assignments.remove(variable)
                    found = false
                    while (!found && stack.isNotEmpty()) {
                        val element = stack.pop()
                        variable = element.variable
                        values = element.values
                        pushDomains = element.pushDomains
                        pushDomains?.forEach { it.popState() }
                        if (values.isNotEmpty()) {
                            found = true
                        } else {
                            assignments.remove(variable)
                        }
                    }
                    if (!found) {
                        return@sequence
                    }
                }

                // Got a value. Check it.
                assignments[variable] = values.removeAt(values.lastIndex)
                pushDomains?.forEach { it.pushState() }
                found = true
                for ((constraint, variables) in vconstraints[variable]!!) {
                    if (!constraint(variables, domains, assignments, !pushDomains.isNullOrEmpty())) {
                        // Value is not good.
                        found = false
                        break
                    }
                }
                if (found) {
                    break
                }
                pushDomains?.forEach { it.popState() }
            }
            // Push state before looking for next variable.
            stack.push(Element(variable, values, pushDomains))
        }
    }

    override fun toString(): String  = "BacktrackingSolver"

    private data class Element<V : Any, D : Any>(
        val variable: V,
        val values: ArrayList<D>,
        val pushDomains: ArrayList<Domain<D>>?
    )
}