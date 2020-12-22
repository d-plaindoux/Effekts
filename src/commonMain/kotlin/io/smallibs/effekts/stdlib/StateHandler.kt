package io.smallibs.effekts.stdlib

import io.smallibs.control.App
import io.smallibs.data.Effect
import io.smallibs.data.StateK
import io.smallibs.effekts.core.Handler

class StateHandler<V>(
    val set: (V) -> Effect<App<StateK<V>, Unit>>,
    val get: Effect<App<StateK<V>, V>>
) : Handler
