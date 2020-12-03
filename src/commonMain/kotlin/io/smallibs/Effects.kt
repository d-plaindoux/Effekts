package io.smallibs

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class Effects<O, E>(var effect: suspend (E) -> Any) {

    suspend fun <A> perform(action: E): A = this.effect(action) as A // ???

    class Handler<O, E>(private val code: suspend Effects<O, E>.() -> O) {
        infix fun with(effects: suspend (E) -> Any): Deferred<O> =
            GlobalScope.async { Effects<O, E>(effects).run { code() } }
    }

    companion object {
        fun <O, E> handle(block: suspend Effects<O, E>.() -> O): Handler<O, E> =
            Handler(block)
    }
}

