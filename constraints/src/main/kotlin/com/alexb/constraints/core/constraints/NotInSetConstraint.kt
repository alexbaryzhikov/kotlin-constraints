package com.alexb.constraints.core.constraints

import com.alexb.constraints.core.Constraint
import com.alexb.constraints.core.Domain
import com.alexb.constraints.utils.ConstraintEnv

/**
 * Constraint enforcing that values of given variables are not present in
 * the given set.
 *
 * @param set Set of disallowed values.
 */
class NotInSetConstraint<V : Any, D : Number>(
    private val set: Set<D>
) : Constraint<V, D> {

    override fun invoke(
        variables: List<V>,
        domains: HashMap<V, Domain<D>>,
        assignments: HashMap<V, D>,
        forwardcheck: Boolean
    ): Boolean {
        throw IllegalStateException("Can't happen")
    }

    override fun preprocess(
        variables: List<V>,
        domains: HashMap<V, Domain<D>>,
        constraints: ArrayList<ConstraintEnv<V, D>>,
        vconstraints: HashMap<V, ArrayList<ConstraintEnv<V, D>>>
    ) {
        for (v in variables) {
            val domain = domains[v]!!
            for (value in ArrayList(domain)) {
                if (value in set) {
                    domain.remove(value)
                }
            }
            vconstraints[v]!!.remove(Pair<Constraint<V, D>, List<V>>(this, variables))
        }
        constraints.remove(Pair<Constraint<V, D>, List<V>>(this, variables))
    }
}
