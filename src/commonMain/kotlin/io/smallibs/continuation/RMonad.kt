package io.smallibs.continuation

import io.smallibs.control.App

interface RMonad<F> {
    fun <A> reflect(x: App<F, A>): A
    fun <A> reify(f: () -> A): App<F, A>
}
