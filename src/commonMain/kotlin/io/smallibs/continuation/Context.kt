package io.smallibs.continuation

internal class Context<A>(
    val state: State<A>,
    val nest: List<State<A>>
) {
    fun setFuture(future: List<Frame>) =
        Context(State(this.state.block, this.state.past, future), this.nest)

    fun addToPast(frame: Frame) =
        Context(State(this.state.block, this.state.past + listOf(frame), this.state.future), this.nest)
}
