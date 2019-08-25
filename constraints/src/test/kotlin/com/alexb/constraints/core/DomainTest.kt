/*
Copyright 2019 Alex Baryzhikov

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package com.alexb.constraints.core

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