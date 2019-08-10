package core

import utils.ConstraintEnv

/**
 * Interface for constraints.
 */
interface Constraint<V : Any, D : Any> {

    /**
     * Perform the constraint checking.
     *
     * If the [forwardcheck] parameter is not false, besides telling if
     * the constraint is currently broken or not, the constraint
     * implementation may choose to hide values from the domains of
     * unassigned variables to prevent them from being used, and thus
     * prune the search space.
     *
     * @param variables Variables affected by that constraint, in the
     *        same order provided by the user.
     * @param domains Dictionary mapping variables to their domains.
     * @param assignments Dictionary mapping assigned variables to their
     *        current assumed value
     * @param forwardcheck Boolean value stating whether forward checking
     *        should be performed or not
     * @return Boolean value stating if this constraint is currently
     *         broken or not
     */
    operator fun invoke(
        variables: List<V>,
        domains: HashMap<V, Domain<D>>,
        assignments: HashMap<V, D>,
        forwardcheck: Boolean = false
    ): Boolean

    /**
     * Preprocess variable domains.
     *
     * This method is called before starting to look for solutions,
     * and is used to prune domains with specific constraint logic
     * when possible. For instance, any constraints with a single
     * variable may be applied on all possible values and removed,
     * since they may act on individual values even without further
     * knowledge about other assignments.
     *
     * @param variables Variables affected by that constraint, in the
     *        same order provided by the user.
     * @param domains Dictionary mapping variables to their domains.
     * @param constraints List of pairs of (constraint, variables)
     * @param vconstraints Dictionary mapping variables to a list of
     *        constraints affecting the given variables.
     */
    fun preprocess(
        variables: List<V>,
        domains: HashMap<V, Domain<D>>,
        constraints: ArrayList<ConstraintEnv<V, D>>,
        vconstraints: HashMap<V, ArrayList<ConstraintEnv<V, D>>>
    ) {
        if (variables.size == 1) {
            val variable = variables[0]
            val domain = domains[variable]!!
            for (value in ArrayList(domain)) {
                if (!this(variables, domains, hashMapOf(variable to value))) {
                    domain.remove(value)
                }
            }
            constraints.remove(Pair(this, variables))
            vconstraints[variable]!!.remove(Pair(this, variables))
        }
    }

    /**
     * Helper method for generic forward checking.
     *
     * Currently, this method acts only when there's a single
     * unassigned variable.
     *
     * @param variables Variables affected by that constraint, in the
     *        same order provided by the user.
     * @param domains Dictionary mapping variables to their domains.
     * @param assignments Dictionary mapping assigned variables to their
     *        current assumed value.
     * @return Boolean value stating if this constraint is currently
     *         broken or not.
     */
    fun forwardCheck(
        variables: List<V>,
        domains: HashMap<V, Domain<D>>,
        assignments: HashMap<V, D>
    ): Boolean {
        var unassignedVariable: V? = null
        for (variable in variables) {
            if (variable !in assignments) {
                if (unassignedVariable == null) {
                    unassignedVariable = variable
                } else {
                    unassignedVariable = null
                    break
                }
            }
        }
        // Case of single unassigned variable.
        if (unassignedVariable != null) {
            // Remove from the unassigned variable domain's all
            // values which break our variable's constraints.
            val domain = domains[unassignedVariable]!!
            if (domain.isNotEmpty()) {
                for (value in ArrayList(domain)) {
                    assignments[unassignedVariable] = value
                    if (!this(variables, domains, assignments)) {
                        domain.hideValue(value)
                    }
                }
                assignments.remove(unassignedVariable)
            }
            if (domain.isEmpty()) {
                return false
            }
        }
        return true
    }
}
