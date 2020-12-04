package io.smallibs.effects

import io.smallibs.core.Effect

sealed class State<V, T> : Effect<T>() {
    data class set<V>(val value: V) : State<V, Unit>()
    class get<V> : State<V, V>()
}
