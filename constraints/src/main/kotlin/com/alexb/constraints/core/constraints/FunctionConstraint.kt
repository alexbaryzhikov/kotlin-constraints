package com.alexb.constraints.core.constraints

import com.alexb.constraints.core.Constraint
import com.alexb.constraints.core.Domain

/**
 * Constraint which wraps a function defining the constraint logic.
 *
 * Example:
 * ```
 *     problem = Problem()
 *     problem.addVariables(listOf("a", "b"), listOf(1, 2))
 *     fun func(args: List<Int>) = args[1] > args[2]
 *     problem.addConstraint(func, listOf("a", "b"))
 *     problem.getSolution()
 * ```
 * Output:
 * ```
 *     {a=1, b=2}
 * ```
 *
 * @param func Function wrapped and queried for constraint logic.
 */
class FunctionConstraint<V : Any, D : Any>(
    private val func: (List<D>) -> Boolean
) : Constraint<V, D> {

    override fun invoke(
        variables: List<V>,
        domains: HashMap<V, Domain<D>>,
        assignments: HashMap<V, D>,
        forwardcheck: Boolean
    ): Boolean {
        val args = variables.map { assignments[it] }
        val missing = args.count { it == null }
        return if (missing != 0) {
            !forwardcheck || missing != 1 || forwardCheck(variables, domains, assignments)
        } else {
            @Suppress("UNCHECKED_CAST")
            func(args as List<D>)
        }
    }

    override fun toString(): String = "FunctionConstraint@${func.hashCode()}"
}
