package io.smallibs.effects

import io.smallibs.core.Effect

sealed class IOConsole<T> : Effect<T>() {
    data class printString(val text: String) : IOConsole<Unit>()
    class readString : IOConsole<String>()
}
