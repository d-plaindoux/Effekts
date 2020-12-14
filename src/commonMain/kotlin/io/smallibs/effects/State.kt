package io.smallibs.effects

import io.smallibs.core.Effect
import io.smallibs.core.Handler

class State<V>(
    val set: (V) -> Effect<Unit>,
    val get: Effect<V>
) : Handler
