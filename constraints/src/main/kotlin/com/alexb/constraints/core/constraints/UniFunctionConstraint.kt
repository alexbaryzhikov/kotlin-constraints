package com.alexb.constraints.core.constraints

import com.alexb.constraints.core.Constraint
import com.alexb.constraints.core.Domain

/**
 * Constraint which wraps a function defining the constraint logic.
 *
 * @param func Function wrapped and queried for constraint logic.
 */
class UniFunctionConstraint<V : Any, D : Any>(
    private val func: (D) -> Boolean
) : Constraint<V, D> {

    override fun invoke(
        variables: List<V>,
        domains: HashMap<V, Domain<D>>,
        assignments: HashMap<V, D>,
        forwardcheck: Boolean
    ): Boolean {
        if (variables.size != 1) {
            throw IllegalArgumentException("Wrong number of arguments")
        }
        val args = variables.map { assignments[it] }
        val missing = args.count { it == null }
        return if (missing != 0) {
            !forwardcheck || forwardCheck(variables, domains, assignments)
        } else {
            func(args[0]!!)
        }
    }
}