package io.smallibs.continuation

import io.smallibs.continuation.thermometer.Context
import io.smallibs.continuation.thermometer.Control
import io.smallibs.continuation.thermometer.Thermometer
import io.smallibs.control.App
import io.smallibs.control.Monad

@Suppress("UNCHECKED_CAST")
class Represent<F>(private val m: Monad<F>, private val cont: Control<App<F, Any>>) : RMonad<F> {
    override fun <A> reflect(x: App<F, A>): A =
        cont.shift { k -> m.bind(x, k) }

    override fun <A> reify(f: () -> A): App<F, A> =
        m.bind(cont.reset { m.returns(f() as Any) }) { x -> m.returns(x as A) }

    constructor(m: Monad<F>) : this(m, Thermometer(Context()))
}
