package core

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class DomainTest {

    @Test
    fun creation() {
        val domain = Domain(setOf(1, 2, 3))
        assertThat(domain).containsExactly(1, 2, 3)
    }

    @Test
    fun pushState() {
        val domain = Domain(setOf(1, 2, 3))

        domain.pushState()
        domain.hideValue(2)
        domain.hideValue(1)
        domain.hideValue(3)
        assertThat(domain).isEmpty()

        domain.popState()
        assertThat(domain).containsExactly(1, 2, 3)
    }

    @Test
    fun resetState() {
        val domain = Domain(setOf(1, 2, 3))
        domain.hideValue(1)
        assertThat(domain).containsExactly(2,3)

        domain.hideValue(3)
        assertThat(domain).containsExactly(2)

        domain.resetState()
        assertThat(domain).containsExactly(1, 2, 3)
    }
}