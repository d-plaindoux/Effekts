package io.smallibs.effects

import io.smallibs.core.Effect

class State<V>(
    val set: (V) -> Effect<Unit>,
    val get: Effect<V>
)
