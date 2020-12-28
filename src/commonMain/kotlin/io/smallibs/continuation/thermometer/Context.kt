package io.smallibs.continuation.thermometer

class Context<A> private constructor(
    val state: State<A>,
    val nest: List<State<A>>
) {
    constructor() : this(State(null, listOf(), listOf()), listOf())

    fun setFuture(future: List<Frame>) =
        Context(State(this.state.block, this.state.past, future), this.nest)

    fun addToPast(frame: Frame) =
        Context(State(this.state.block, this.state.past + listOf(frame), this.state.future), this.nest)

    fun switch(f: () -> A, future: List<Frame>) =
        Context(State(f, listOf(), future), this.nest + listOf(this.state))

    fun returns(): Context<A> {
        val (prev, nest) = this.nest.pop()
        return Context(prev, nest)
    }
}
