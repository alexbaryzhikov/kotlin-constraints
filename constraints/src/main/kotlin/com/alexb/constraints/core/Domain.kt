package com.alexb.constraints.core

import java.util.*
import kotlin.collections.ArrayList

/**
 * Class used to control possible values for variables
 *
 * When list or tuples are used as domains, they are automatically
 * converted to an instance of that class.
 *
 * @param set Set of values that the given variables may assume.
 */
class Domain<D : Any>(set: Set<D>) : ArrayList<D>(set) {

    private val hidden = ArrayList<D>()
    private val states: Deque<Int> = ArrayDeque()

    /**
     * Reset to the original domain state, including all possible values.
     */
    fun resetState() {
        addAll(hidden)
        hidden.clear()
        states.clear()
    }

    /**
     * Save current domain state.
     *
     * Variables hidden after that call are restored when that state
     * is popped from the stack.
     */
    fun pushState() {
        states.push(size)
    }

    /**
     * Restore domain state from the top of the stack
     *
     * Variables hidden since the last popped state are then available
     * again.
     */
    fun popState() {
        val diff = states.pop() - size
        if (diff != 0) {
            addAll(hidden.subList(hidden.size - diff, hidden.size))
            repeat(diff) { hidden.removeAt(hidden.lastIndex) }
        }
    }

    /**
     * Hide the given value from the domain.
     *
     * After that call the given value won't be seen as a possible value
     * on that domain anymore. The hidden value will be restored when the
     * previous saved state is popped.
     *
     * @param value Object currently available in the domain.
     */
    fun hideValue(value: D) {
        if (!remove(value)) {
            throw IllegalArgumentException("Value not found: $value")
        }
        hidden.add(value)
    }
}
