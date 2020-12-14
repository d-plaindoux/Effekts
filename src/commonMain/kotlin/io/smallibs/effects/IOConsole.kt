package io.smallibs.effects

import io.smallibs.core.Effect

class IOConsole(
    val printString: (String) -> Effect<Unit>,
    val readString: Effect<String>
)

