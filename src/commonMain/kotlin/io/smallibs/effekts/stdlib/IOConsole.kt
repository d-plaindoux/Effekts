package io.smallibs.effekts.stdlib

import io.smallibs.data.Effect
import io.smallibs.effekts.core.Handler

class IOConsole(
    val printString: (String) -> Effect<Unit>,
    val readString: Effect<String>
) : Handler
