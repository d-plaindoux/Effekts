package io.smallibs.effects

import io.smallibs.effect.Effect
import io.smallibs.effect.Handler

class State<V>(
    val set: (V) -> Effect<Unit>,
    val get: Effect<V>
) : Handler
