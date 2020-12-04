package io.smallibs.core

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class Handler<O, E : Effect<*>>(private val code: suspend Effects<O, E>.() -> O) {
    infix fun with(effects: suspend (E) -> Any?): Deferred<O> =
        GlobalScope.async { Effects<O, E>(effects).run { code() } } // Execution should be reviewed

    companion object {
        fun <O, E : Effect<*>> handle(block: suspend Effects<O, E>.() -> O): Handler<O, E> =
            Handler(block)
    }
}
