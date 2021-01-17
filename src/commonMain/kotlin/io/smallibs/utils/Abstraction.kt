package io.smallibs.utils

interface Abstraction<A, B> {
    operator fun invoke(a: A): B
    fun reify(): (A) -> B = { this(it) }
}
