package io.smallibs.continuation

//
// See https://github.com/jkoppel/thermometer-continuations
// for the original implementation in Java, OCaml and SML.
//
// Paper: https://dl.acm.org/doi/pdf/10.1145/3236771
//

class Thermometer<A> private constructor(private var context: Context<A>) : Control<A> {

    override fun reset(block: () -> A): A {
        return run(block, listOf())
    }

    @Suppress("UNCHECKED_CAST")
    override fun <B : Any> shift(f: ((B) -> A) -> A): A {
        val (frame, future) = context.state.future.pop(Frame.Enter)
        context = context.setFuture(future)

        when (frame) {
            is Frame.Return -> {
                context = context.addToPast(frame)
                return frame.value as A
            }
            is Frame.Enter -> {
                val newFuture = context.state.past.reversed()
                val block = context.state.block
                val k = { v: B ->
                    run(block!!, newFuture + Frame.Return(v))
                }
                context = context.addToPast(Frame.Enter)
                throw Done(f(k))
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun run(f: (() -> A), future: List<Frame>): A =
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
