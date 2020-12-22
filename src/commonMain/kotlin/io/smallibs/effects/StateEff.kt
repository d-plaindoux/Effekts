package io.smallibs.effects

import io.smallibs.data.Effect
import io.smallibs.effect.Handler

class StateEff<V>(
    val set: (V) -> Effect<Unit>,
    val get: Effect<V>
) : Handler
