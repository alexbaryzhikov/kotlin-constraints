# Constraints
[![Maven](https://img.shields.io/maven-metadata/v/https/dl.bintray.com/alexb/kotlin-constraints/com/alexb/constraints/maven-metadata.xml)](https://bintray.com/alexb/kotlin-constraints/constraints/_latestVersion)

A constraints satisfaction solver for Kotlin.

The Kotlin constraints library offers solvers for Constraint Satisfaction Problems (CSPs) over finite domains. CSP is class of problems which may be represented in terms of variables (a, b, ...), domains (a in [1, 2, 3], ...), and constraints (a < b, ...).

## Examples

#### Basics

This example demonstrates basic library operations:
```Kotlin
import com.alexb.constraints.Problem

var problem = Problem<String, Int>().apply { 
    addVariable("a", listOf(1, 2, 3))
    addVariable("b", listOf(4, 5, 6))
}

println(problem.getSolutions())
[{a=1, b=4}, {a=1, b=5}, {a=1, b=6}, {a=2, b=4}, {a=2, b=5}, {a=2, b=6}, {a=3, b=4}, {a=3, b=5}, {a=3, b=6}]

problem.addConstraint({ a, b -> a * 2 == b }, listOf("a", "b"))

println(problem.getSolutions())
[{a=2, b=4}, {a=3, b=6}]

import com.alexb.constraints.core.constraints.AllDifferentConstraint

problem = Problem<String, Int>().apply {
    addVariables(listOf("a", "b"), listOf(1, 2, 3))
    addConstraint(AllDifferentConstraint())
}

println(problem.getSolutions())
[{a=1, b=2}, {a=1, b=3}, {a=2, b=3}, {a=2, b=1}, {a=3, b=1}, {a=3, b=2}]
```

#### Rooks problem

The following example solves the classical Eight Rooks problem:
```Kotlin
import com.alexb.constraints.Problem

val numPieces = 8
val cols = (0 until numPieces).toList()
val rows = (0 until numPieces).toList()
val problem = Problem<Int, Int>().apply {
    addVariables(cols, rows)
    for (col1 in cols) {
        for (col2 in cols) {
            if (col1 < col2) {
                addConstraint({ row1, row2 -> row1 != row2 }, listOf(col1, col2))
            }
        }
    }
}
val solutions = problem.getSolutions()

println(solutions.size)
40320

println(solutions.random())
{0=2, 1=3, 2=1, 3=7, 4=4, 5=5, 6=6, 7=0}
```

#### Magic squares

This example solves a 4x4 magic square:
```Kotlin
import com.alexb.constraints.Problem
import com.alexb.constraints.core.constraints.AllDifferentConstraint
import com.alexb.constraints.core.constraints.ExactSumConstraint
    
val problem = Problem<Int, Int>().apply {
    addVariables((0 until 16).toList(), (1 until 17).toList())
    addConstraint(AllDifferentConstraint(), (0 until 16).toList())
    addConstraint(ExactSumConstraint(34), listOf(0, 5, 10, 15))
    addConstraint(ExactSumConstraint(34), listOf(3, 6, 9, 12))
    for (row in 0 until 4) {
        addConstraint(ExactSumConstraint(34), List(4) { i -> row * 4 + i })
    }
    for (col in 0 until 4) {
        addConstraint(ExactSumConstraint(34), List(4) { i -> col + 4 * i })
    }
}

val solutions = problem.getSolutions()

println(solutions.size)
7040

println(solutions.random())
{0=6, 1=10, 2=3, 3=15, 4=11, 5=7, 6=14, 7=2, 8=13, 9=1, 10=12, 11=8, 12=4, 13=16, 14=5, 15=9}
```

## Features

The following solvers are available:
* Backtracking solver
* Recursive backtracking solver
* Minimum conflicts solver

Predefined constraint types currently available:
* FunctionConstraint
* UniFunctionConstraint
* BiFunctionConstraint
* TriFunctionConstraint
* AllDifferentConstraint
* AllEqualConstraint
* ExactSumConstraint
* MaxSumConstraint
* MinSumConstraint
* InSetConstraint
* NotInSetConstraint
* SomeInSetConstraint
* SomeNotInSetConstraint

## Installation

#### Gradle

Resolving artifacts:
```
repositories {
    maven {
        url  "https://dl.bintray.com/alexb/kotlin-constraints" 
    }
}
```

Including dependency:
```
dependencies {
    implementation 'com.alexb:constraints:0.1.x'
}
```

## Credits

This library is a Kotlin port of Gustavo Niemeyer's python module [python-constraint](https://github.com/python-constraint/python-constraint).

## License
```
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
```
