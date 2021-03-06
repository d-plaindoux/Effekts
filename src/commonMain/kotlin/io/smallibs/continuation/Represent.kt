package io.smallibs.continuation

import io.smallibs.continuation.thermometer.Context
import io.smallibs.continuation.thermometer.Control
import io.smallibs.continuation.thermometer.Thermometer
import io.smallibs.control.App
import io.smallibs.control.Monad

class Represent<F>(private val m: Monad<F>, private val cont: Control<App<F, Any>> = Thermometer(Context())) :
    RMonad<F> {
    override fun <A> reflect(x: App<F, A>): A =
        cont.shift { k -> m.bind(x, k) }

    @Suppress("UNCHECKED_CAST")
    override fun <A> reify(f: () -> A): App<F, A> =
        m.bind(cont.reset { m.returns(f() as Any) }) { x -> m.returns(x as A) }
}
