package io.smallibs.core

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class Effects<O, H : Handler>(private val block: suspend Effects<O, H>.(H) -> O) {

    infix fun with(effect: () -> H): Deferred<O> =
        with(effect())

    infix fun with(effect: H): Deferred<O> =
        GlobalScope.async { run { block(effect) } } // Execution should be reviewed

    suspend fun <A> Effect<A>.bind(): A = this{ v ->
        suspendCoroutine { cont -> cont.resume(v) }
    }

    companion object {
        fun <O, H : Handler> handle(block: suspend Effects<O, H>.(H) -> O) =
            Effects(block)
    }
}
