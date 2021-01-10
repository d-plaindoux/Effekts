package io.smallibs.continuation.thermometer

data class Stack<A>(internal val value: List<A>) {
    constructor(a: A) : this(listOf(a))
    constructor() : this(listOf())

    fun push(a: A): Stack<A> = Stack(listOf(a) + this.value)

    fun pop(): Pair<A, Stack<A>> =
        this.value[0] to Stack(this.value.subList(1, this.value.size))

    internal fun pop(d: A): Pair<A, Stack<A>> =
        if (this.value.isEmpty()) {
            d to this
        } else {
            this.pop()
        }

    fun reversed(): Stack<A> =
        Stack(this.value.reversed())

    operator fun plus(s: Stack<A>): Stack<A> = Stack(this.value + s.value)
}
