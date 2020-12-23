package io.smallibs.continuation

//
// See https://github.com/jkoppel/thermometer-continuations
// for the original implementation in Java, OCaml and SML.
//
// Paper: https://dl.acm.org/doi/pdf/10.1145/3236771
//

class Thermometer<A> private constructor(private var context: Context<A>) : Control<A> {

    override fun reset(block: () -> A): A {
        return runWithFuture(block, listOf())
    }

    override fun <B : Any> shift(f: ((B) -> A) -> A): A {
        val (frame, future) = context.state.future.pop(Frame.Enter)
        context = context.setFuture(future)

        when (frame) {
            is Frame.Return -> {
                context = context.addToPast(frame)
                return frame.value as A
            }
            is Frame.Enter -> {
                val future = context.state.past.reversed()
                val block = context.state.block
                val k = { v: B ->
                    runWithFuture(block!!, future + Frame.Return(v))
                }
                context = context.addToPast(Frame.Enter)
                throw Done(f(k))
            }
        }
    }

    private fun runWithFuture(f: (() -> A), future: List<Frame>): A =
        try {
            context = Context(State(f, listOf(), future), context.nest + listOf(context.state))
            f()
        } catch (d: Done) {
            d.value as A // Because of exception limitation
        } finally {
            val (prev, nest) = context.nest.pop()
            context = Context(prev, nest)
        }

    companion object {
        private operator fun <A> invoke() =
            Thermometer<A>(Context(State(null, listOf(), listOf()), listOf()))

        fun <A> run(block: Thermometer<A>.() -> A) =
            Thermometer<A>().run(block)
    }
}
