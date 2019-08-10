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
