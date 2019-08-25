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

package com.alexb.constraints.utils

import com.alexb.constraints.core.Domain
import kotlin.math.round

@Suppress("UNCHECKED_CAST")
internal fun <D : Number> D.zero(): D = when (this) {
    is Byte -> 0 as D
    is Short -> 0 as D
    is Int -> 0 as D
    is Long -> 0L as D
    is Float -> 0.0f as D
    is Double -> 0.0 as D
    else -> throw IllegalArgumentException("Unsupported number type")
}

internal fun Double.round(n: Int): Double {
    var m = 1.0
    repeat(n) { m *= 10 }
    return round(this * m) / m
}

@Suppress("UNCHECKED_CAST")
internal operator fun <D : Number> D.plus(other: D): D = when (this) {
    is Byte -> when (other) {
        is Byte -> plus(other).toByte() as D
        else -> throw IllegalArgumentException("Operands types mismatch")
    }
    is Short -> when (other) {
        is Short -> plus(other).toShort() as D
        else -> throw IllegalArgumentException("Operands types mismatch")
    }
    is Int -> when (other) {
        is Int -> plus(other) as D
        else -> throw IllegalArgumentException("Operands types mismatch")
    }
    is Long -> when (other) {
        is Long -> plus(other) as D
        else -> throw IllegalArgumentException("Operands types mismatch")
    }
    is Float -> when (other) {
        is Float -> plus(other) as D
        else -> throw IllegalArgumentException("Operands types mismatch")
    }
    is Double -> when (other) {
        is Double -> plus(other) as D
        else -> throw IllegalArgumentException("Operands types mismatch")
    }
    else -> throw IllegalArgumentException("Unsupported number type")
}

internal operator fun Number.compareTo(other: Number): Int = when (this) {
    is Byte -> when (other) {
        is Byte -> compareTo(other)
        is Short -> compareTo(other)
        is Int -> compareTo(other)
        is Long -> compareTo(other)
        is Float -> compareTo(other)
        is Double -> compareTo(other)
        else -> throw IllegalArgumentException("Unsupported number type")
    }
    is Short -> when (other) {
        is Byte -> compareTo(other)
        is Short -> compareTo(other)
        is Int -> compareTo(other)
        is Long -> compareTo(other)
        is Float -> compareTo(other)
        is Double -> compareTo(other)
        else -> throw IllegalArgumentException("Unsupported number type")
    }
    is Int -> when (other) {
        is Byte -> compareTo(other)
        is Short -> compareTo(other)
        is Int -> compareTo(other)
        is Long -> compareTo(other)
        is Float -> compareTo(other)
        is Double -> compareTo(other)
        else -> throw IllegalArgumentException("Unsupported number type")
    }
    is Long -> when (other) {
        is Byte -> compareTo(other)
        is Short -> compareTo(other)
        is Int -> compareTo(other)
        is Long -> compareTo(other)
        is Float -> compareTo(other)
        is Double -> compareTo(other)
        else -> throw IllegalArgumentException("Unsupported number type")
    }
    is Float -> when (other) {
        is Byte -> compareTo(other)
        is Short -> compareTo(other)
        is Int -> compareTo(other)
        is Long -> compareTo(other)
        is Float -> compareTo(other)
        is Double -> compareTo(other)
        else -> throw IllegalArgumentException("Unsupported number type")
    }
    is Double -> when (other) {
        is Byte -> compareTo(other)
        is Short -> compareTo(other)
        is Int -> compareTo(other)
        is Long -> compareTo(other)
        is Float -> compareTo(other)
        is Double -> compareTo(other)
        else -> throw IllegalArgumentException("Unsupported number type")
    }
    else -> throw IllegalArgumentException("Unsupported number type")
}

internal fun <V : Any, D : Any> HashMap<V, Domain<D>>.copy(): HashMap<V, Domain<D>> {
    val result = HashMap<V, Domain<D>>()
    for (entry in entries) {
        result[entry.key] = Domain(entry.value.toSet())
    }
    return result
}
