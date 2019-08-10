package com.alexb.constraints

import com.alexb.constraints.core.Constraint
import com.alexb.constraints.core.Domain
import com.alexb.constraints.core.Solver
import com.alexb.constraints.core.constraints.BiFunctionConstraint
import com.alexb.constraints.core.constraints.FunctionConstraint
import com.alexb.constraints.core.constraints.TriFunctionConstraint
import com.alexb.constraints.core.constraints.UniFunctionConstraint
import com.alexb.constraints.core.solvers.BacktrackingSolver
import com.alexb.constraints.utils.ConstraintEnv
import com.alexb.constraints.utils.copy

/**
 * Class used to define a problem and retrieve solutions.
 *
 * @param solver Problem solver used to find solutions
 *        (default is [BacktrackingSolver]).
 */
class Problem<V : Any, D : Any>(var solver: Solver<V, D> = BacktrackingSolver()) {

    private val constraints: ArrayList<ConstraintEnv<V, D>> = ArrayList()
    private val domains: HashMap<V, Domain<D>> = HashMap()

    /**
     * Reset the current problem definition.
     *
     * Example:
     * ```
     *     problem = Problem()
     *     problem.addVariable("a", listOf(1, 2))
     *     problem.reset()
     *     problem.getSolution()
     * ```
     * Output:
     * ```
     *     null
     * ```
     */
    fun reset() {
        constraints.clear()
        domains.clear()
    }

    /**
     * Add a variable to the problem

     * Example:
     * ```
     *     problem = Problem()
     *     problem.addVariable("a", listOf(1, 2))
     *     problem.getSolution() in listOf(mapOf("a" to 1), mapOf("a" to 2))
     * ```
     * Output:
     * ```
     *     true
     * ```
     *
     * @param variable Object representing a problem variable.
     * @param domain Set of items defining the possible values that
     *        the given variable may assume.
     */
    fun addVariable(variable: V, domain: List<D>) {
        if (variable in domains) {
            throw IllegalArgumentException("Duplicate variable: $variable")
        }
        if (domain.isEmpty()) {
            throw IllegalArgumentException("Domain is empty")
        }
        domains[variable] = Domain(domain.toSet())
    }

    /**
     * Add one or more variables to the problem.
     *
     * Example:
     * ```
     *     problem = Problem()
     *     problem.addVariables(listOf("a", "b"), listOf(1, 2, 3))
     *     solutions = problem.getSolutions()
     *     solutions.toList().size
     * ```
     * Output:
     * ```
     *     9
     * ```
     *
     * @param variables A collection of objects representing problem variables.
     * @param domain Set of items defining the possible values that
     *        the given variables may assume.
     */
    fun addVariables(variables: List<V>, domain: List<D>) {
        for (variable in variables) {
            addVariable(variable, domain)
        }
    }

    /**
     * Add a constraint to the problem.
     *
     * @param constraint Constraint to be included in the problem.
     * @param variables Variables affected by the constraint (default to
     *        all variables). Depending on the constraint type
     *        the order may be important.
     */
    fun addConstraint(constraint: Constraint<V, D>, variables: List<V> = emptyList()) {
        constraints.add(Pair(constraint, ArrayList(variables)))
    }

    /**
     * Add a constraint to the problem.
     *
     * Example:
     *```
     *     problem = Problem()
     *     problem.addVariables(listOf("a", "b"), listOf(1, 2, 3))
     *     problem.addConstraint({ args -> args[1] == args[0] + 1 }, listOf("a", "b"))
     *     solutions = problem.getSolutions()
     * ```
     * Output:
     * ```
     *     [{a=1, b=2}, {a=2, b=3}]
     * ```
     *
     * @param constraint Constraint to be included in the problem.
     * @param variables Variables affected by the constraint (default to
     *        all variables). Depending on the constraint type
     *        the order may be important.
     */
    fun addConstraint(constraint: (List<D>) -> Boolean, variables: List<V> = emptyList()) {
        constraints.add(Pair(FunctionConstraint(constraint), ArrayList(variables)))
    }

    /**
     * Add a constraint to the problem.
     *
     * Example:
     * ```
     *     problem = Problem()
     *     problem.addVariables(listOf("a", "b", "c"), listOf(1, 2, 3))
     *     problem.addConstraint({ a, b, c -> a + b == c }, listOf("a", "b", "c"))
     *     problem.getSolution()
     * ```
     * Output:
     * ```
     *     {a=1, b=1, c=2}
     * ```
     *
     * @param constraint Constraint to be included in the problem.
     * @param variables Variables affected by the constraint (default to
     *        all variables). Depending on the constraint type
     *        the order may be important.
     */
    fun addConstraint(constraint: (D, D, D) -> Boolean, variables: List<V> = emptyList()) {
        constraints.add(Pair(TriFunctionConstraint(constraint), ArrayList(variables)))
    }

    /**
     * Add a constraint to the problem.
     *
     * Example:
     *```
     *     problem = Problem()
     *     problem.addVariables(listOf("a", "b"), listOf(1, 2, 3))
     *     problem.addConstraint({ a, b -> b == a + 1 }, listOf("a", "b"))
     *     solutions = problem.getSolutions()
     * ```
     * Output:
     * ```
     *     [{a=1, b=2}, {a=2, b=3}]
     * ```
     *
     * @param constraint Constraint to be included in the problem.
     * @param variables Variables affected by the constraint (default to
     *        all variables). Depending on the constraint type
     *        the order may be important.
     */
    fun addConstraint(constraint: (D, D) -> Boolean, variables: List<V> = emptyList()) {
        constraints.add(Pair(BiFunctionConstraint(constraint), ArrayList(variables)))
    }

    /**
     * Add a constraint to the problem.
     *
     * Example:
     * ```
     *     problem = Problem()
     *     problem.addVariables(listOf("a", "b"), listOf(1, 2))
     *     problem.addConstraint({ a -> a == 2 }, "a")
     *     problem.getSolution()
     * ```
     * Output:
     * ```
     *     [{a=2, b=1}, {a=2, b=2}]
     * ```
     *
     * @param constraint Constraint to be included in the problem.
     * @param variable Variable affected by the constraint.
     */
    fun addConstraint(constraint: (D) -> Boolean, variable: V) {
        constraints.add(Pair(UniFunctionConstraint(constraint), arrayListOf(variable)))
    }

    /**
     * Find and return a solution to the problem.
     *
     * Example:
     * ```
     *     problem = Problem()
     *     problem.getSolution() == null
     * ```
     * Output:
     * ```
     *     true
     *
     *     problem.addVariable("a", listOf(42))
     *     problem.getSolution()
     * ```
     * Output:
     * ```
     *     {a=42}
     * ```
     *
     * @return Solution for the problem.
     */
    fun getSolution(): Map<V, D>? {
        val (domains, constraints, vconstraints) = getArgs()
        if (domains.isEmpty()) {
            return null
        }
        return solver.getSolution(domains, constraints, vconstraints)
    }

    /**
     * Find and return all solutions to the problem.
     *
     * Example:
     * ```
     *     problem = Problem()
     *     problem.getSolutions().isEmpty()
     * ```
     * Output:
     * ```
     *     true
     *
     *     problem.addVariable("a", listOf(42))
     *     problem.getSolutions()
     * ```
     * Output:
     * ```
     *     [{a=42}]
     * ```
     *
     * @return All solutions for the problem.
     */
    fun getSolutions(): List<Map<V, D>> {
        val (domains, constraints, vconstraints) = getArgs()
        if (domains.isEmpty()) {
            return emptyList()
        }
        return solver.getSolutions(domains, constraints, vconstraints)
    }

    /**
     * Find and return all solutions to the problem.
     *
     * Example:
     * ```
     *     problem = Problem()
     *     problem.getSolutionSequence().toList().isEmpty()
     * ```
     * Output:
     * ```
     *     true
     *
     *     problem.addVariable("a", listOf(42))
     *     solutions = problem.getSolutionSequence()
     *     solutions.forEach { println(it) }
     * ```
     * Output:
     * ```
     *     {a=42}
     * ```
     *
     * @return All solutions for the problem.
     */
    fun getSolutionSequence(): Sequence<Map<V, D>> {
        val (domains, constraints, vconstraints) = getArgs()
        if (domains.isEmpty()) {
            return emptySequence()
        }
        return solver.getSolutionSequence(domains, constraints, vconstraints)
    }

    private fun getArgs(): Args<V, D> {
        val domains = this.domains.copy()
        val allVariables = domains.keys
        val constraints = ArrayList<ConstraintEnv<V, D>>()
        for ((constraint, variables) in this.constraints) {
            val vs = if (variables.isEmpty()) ArrayList(allVariables) else variables
            constraints.add(Pair(constraint, vs))
        }
        val vconstraints = HashMap<V, ArrayList<ConstraintEnv<V, D>>>()
        for (variable in allVariables) {
            vconstraints[variable] = ArrayList()
        }
        for ((constraint, variables) in constraints) {
            for (variable in variables) {
                vconstraints[variable]!!.add(Pair(constraint, variables))
            }
        }
        // Prune domains.
        for ((constraint, variables) in ArrayList(constraints)) {
            constraint.preprocess(variables, domains, constraints, vconstraints)
        }
        for (domain in domains.values) {
            domain.resetState()
            if (domain.isEmpty()) {
                return Args(hashMapOf(), arrayListOf(), hashMapOf())
            }
        }
        return Args(domains, constraints, vconstraints)
    }

    private data class Args<V : Any, D : Any>(
        val domains: HashMap<V, Domain<D>>,
        val constraints: ArrayList<ConstraintEnv<V, D>>,
        val vconstraints: HashMap<V, ArrayList<ConstraintEnv<V, D>>>
    )
}