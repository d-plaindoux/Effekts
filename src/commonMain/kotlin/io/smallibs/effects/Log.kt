package io.smallibs.effects

import io.smallibs.core.Effect

sealed class Log<T> : Effect<T>() {
    data class log(val value: String) : Log<Unit>()
}
