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

package com.alexb.constraints.core

import com.alexb.constraints.utils.ConstraintEnv

/** Interface for solvers. */
interface Solver<V : Any, D : Any> {

    /**
     * @param domains Dictionary mapping variables to their domains
     * @param constraints List of pairs of (constraint, variables)
     * @param vconstraints Dictionary mapping variables to a list of
     *        constraints affecting the given variables.
     * @return One solution for the given problem.
     */
    fun getSolution(
        domains: HashMap<V, Domain<D>>,
        constraints: ArrayList<ConstraintEnv<V, D>>,
        vconstraints: HashMap<V, ArrayList<ConstraintEnv<V, D>>>
    ): Map<V, D>?

    /**
     * @param domains Dictionary mapping variables to their domains
     * @param constraints List of pairs of (constraint, variables)
     * @param vconstraints Dictionary mapping variables to a list of
     *        constraints affecting the given variables.
     * @return All solutions for the given problem.
     */
    fun getSolutions(
        domains: HashMap<V, Domain<D>>,
        constraints: ArrayList<ConstraintEnv<V, D>>,
        vconstraints: HashMap<V, ArrayList<ConstraintEnv<V, D>>>
    ): List<Map<V, D>>

    /**
     * @param domains Dictionary mapping variables to their domains
     * @param constraints List of pairs of (constraint, variables)
     * @param vconstraints Dictionary mapping variables to a list of
     *        constraints affecting the given variables.
     * @return A [Sequence] of the solutions for the given problem.
     */
    fun getSolutionSequence(
        domains: HashMap<V, Domain<D>>,
        constraints: ArrayList<ConstraintEnv<V, D>>,
        vconstraints: HashMap<V, ArrayList<ConstraintEnv<V, D>>>
    ): Sequence<Map<V, D>>
}
