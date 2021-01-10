package io.smallibs.continuation.thermometer

class Thermometer<A>(var context: Context<A>) : Control<A> {

    override fun reset(block: () -> A): A {
        return run(block, Stack())
    }

    @Suppress("UNCHECKED_CAST")
    override fun <B> shift(f: ((B) -> A) -> A): B {
        val (frame, future) = context.state.future.pop(Frame.Enter)
        context = context.setFuture(future)

        when (frame) {
            is Frame.Return -> {
                context = context.addToPast(frame)
                return frame.value as B // So each returned value is a A (by cast)
            }
            is Frame.Enter -> {
                val newFuture = context.state.past.reversed()
                val block = context.state.block
                val k = { v: B ->
                    run(block!!, newFuture.push(Frame.Return(v as Any)))
                }
                context = context.addToPast(Frame.Enter)
                throw Done(f(k))
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun run(f: (() -> A), future: Stack<Frame>): A =
        try {
            context = context.switch(f, future)
            f()
        } catch (d: Done) {
            d.value as A // Because of exception limitation
        } finally {
            context = context.returns()
        }

    companion object {
        fun <A> run(block: Thermometer<A>.() -> A) =
            Thermometer<A>(Context()).run(block)
    }
}
