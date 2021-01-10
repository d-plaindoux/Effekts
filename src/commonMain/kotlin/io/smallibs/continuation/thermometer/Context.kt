package io.smallibs.continuation.thermometer

class Context<A> private constructor(
    val state: State<A>,
    val nest: Stack<State<A>>
) {
    constructor() : this(State(null, Stack(), Stack()), Stack())

    fun setFuture(future: Stack<Frame>) =
        Context(State(this.state.block, this.state.past, future), this.nest)

    fun addToPast(frame: Frame) =
        Context(State(this.state.block, this.state.past + Stack(frame), this.state.future), this.nest)

    fun switch(f: () -> A, future: Stack<Frame>) =
        Context(State(f, Stack(), future), this.nest + Stack(this.state))

    fun returns(): Context<A> {
        val (prev, nest) = this.nest.pop()
        return Context(prev, nest)
    }
}
