package io.smallibs.effects

import io.smallibs.core.Effect

interface State<V> {
    fun set(value: V) : Effect<Unit>
    fun get() : Effect<V>
}
