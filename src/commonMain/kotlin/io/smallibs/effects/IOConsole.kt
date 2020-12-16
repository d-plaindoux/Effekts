package io.smallibs.effects

import io.smallibs.effect.Effect
import io.smallibs.effect.Handler

class IOConsole(
    val printString: (String) -> Effect<Unit>,
    val readString: Effect<String>
) : Handler

