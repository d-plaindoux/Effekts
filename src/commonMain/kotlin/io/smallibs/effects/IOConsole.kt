package io.smallibs.effects

import io.smallibs.core.Effect

interface IOConsole {
    fun printString(text: String) : Effect<Unit>
    fun readString() : Effect<String>
}

