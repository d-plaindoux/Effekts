package io.smallibs.effects

import io.smallibs.core.Effect

interface Log {
    fun log(value: String): Effect<Unit>
}

