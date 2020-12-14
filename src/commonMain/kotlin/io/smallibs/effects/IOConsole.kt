package io.smallibs.effects

import io.smallibs.core.Effect
import io.smallibs.core.Handler

class IOConsole(
    val printString: (String) -> Effect<Unit>,
    val readString: Effect<String>
) : Handler

