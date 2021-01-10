package io.smallibs.continuation.thermometer

class State<A>(
    val block: (() -> A)?,
    val past: Stack<Frame>,
    val future: Stack<Frame>
)
