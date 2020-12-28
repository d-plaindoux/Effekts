package io.smallibs.continuation.thermometer

interface Control<A> {
    fun reset(block: () -> A): A
    fun <B> shift(f: ((B) -> A) -> A): B
}
