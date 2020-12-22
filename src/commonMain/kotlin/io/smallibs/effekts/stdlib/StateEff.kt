package io.smallibs.effekts.stdlib

import io.smallibs.data.Effect
import io.smallibs.effekts.core.Handler

class StateEff<V>(
    val set: (V) -> Effect<Unit>,
    val get: Effect<V>
) : Handler
