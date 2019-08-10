package com.alexb.constraints.utils

import com.alexb.constraints.core.Constraint

internal typealias ConstraintEnv<V, D> = Pair<Constraint<V, D>, ArrayList<V>>
