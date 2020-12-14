package io.smallibs.core

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class Effects<O, E>(private val block: suspend Effects<O, E>.(E) -> O) {

    infix fun with(effect: () -> E): Deferred<O> =
        GlobalScope.async { run { block(effect()) } } // Execution should be reviewed

    suspend fun <A> Effect<A>.bind(): A = this.resume(continuation())

    private fun <T> continuation(): suspend (T) -> T = { v ->
        suspendCoroutine { cont -> cont.resume(v) }
    }

    companion object {
        fun <O, E> handle(block: suspend Effects<O, E>.(E) -> O): Effects<O, E> =
            Effects(block)
    }
}
