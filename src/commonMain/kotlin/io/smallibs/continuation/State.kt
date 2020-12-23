package io.smallibs.continuation

internal class State<A>(
    val block: (() -> A)?,
    val past: List<Frame>,
    val future: List<Frame>
)
