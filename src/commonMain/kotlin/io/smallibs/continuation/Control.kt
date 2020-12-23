package io.smallibs.continuation

interface Control<A> {
    fun reset(block: () -> A): A
    fun <B : Any> shift(f: ((B) -> A) -> A): A
}
