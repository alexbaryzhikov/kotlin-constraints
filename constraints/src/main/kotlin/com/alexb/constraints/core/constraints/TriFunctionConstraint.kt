package com.alexb.constraints.core.constraints

import com.alexb.constraints.core.Constraint
import com.alexb.constraints.core.Domain

/**
 * Constraint which wraps a function defining the constraint logic.
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
 * @param func Function wrapped and queried for constraint logic.
 */
class TriFunctionConstraint<V : Any, D : Any>(
    private val func: (D, D, D) -> Boolean
) : Constraint<V, D> {

    override fun invoke(
        variables: List<V>,
        domains: HashMap<V, Domain<D>>,
        assignments: HashMap<V, D>,
        forwardcheck: Boolean
    ): Boolean {
        if (variables.size != 3) {
            throw IllegalArgumentException("Wrong number of arguments")
        }
        val args = variables.map { assignments[it] }
        val missing = args.count { it == null }
        return if (missing != 0) {
            !forwardcheck || missing != 1 || forwardCheck(variables, domains, assignments)
        } else {
            func(args[0]!!, args[1]!!, args[2]!!)
        }
    }
}