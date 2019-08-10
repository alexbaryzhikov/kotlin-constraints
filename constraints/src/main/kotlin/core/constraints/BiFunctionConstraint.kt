package core.constraints

import core.Constraint
import core.Domain

/**
 * Constraint which wraps a function defining the constraint logic.
 *
 * Example:
 * ```
 *     problem = Problem()
 *     problem.addVariables(listOf("a", "b"), listOf(1, 2))
 *     problem.addConstraint({ a, b -> b > a }, listOf("a", "b"))
 *     problem.getSolution()
 * ```
 * Output:
 * ```
 *     {a=1, b=2}
 * ```
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