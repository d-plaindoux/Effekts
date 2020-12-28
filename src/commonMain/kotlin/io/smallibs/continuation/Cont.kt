package io.smallibs.continuation

import io.smallibs.control.App

interface Cont<B> {
    fun <A> value(): ((B) -> A) -> A

    companion object {
        fun <A, B, C> run(f: (C) -> Cont<B>): (C) -> ((B) -> A) -> A = { x ->
            { k ->
                f(x).value<A>()(k)
            }
        }
    }
}

class ContK {
    companion object {
        @Suppress("UNCHECKED_CAST")
        fun <A> App<ContK, A>.fix(): Cont<A> = this as Cont<A>
    }
}


