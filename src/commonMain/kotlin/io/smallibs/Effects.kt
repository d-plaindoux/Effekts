package io.smallibs

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class Effects<E, O>(var effect: suspend (E) -> Any) {

    suspend fun <A> perform(action: E): A = this.effect(action) as A // ???

    class Handler<E, O>(private val code: suspend Effects<E, O>.() -> O) {
        infix fun with(effects: suspend (E) -> Any): Deferred<O> =
            GlobalScope.async { Effects<E, O>(effects).run { code() } }
    }

    companion object {
        fun <E, O> handle(block: suspend Effects<E, O>.() -> O): Handler<E, O> =
            Handler(block)
    }
}

