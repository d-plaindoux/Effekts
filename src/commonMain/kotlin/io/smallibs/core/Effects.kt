package io.smallibs.core

import kotlin.coroutines.suspendCoroutine

class Effects<O, H : Handler>(private val block: suspend Effects<O, H>.(H) -> O) {

    suspend infix fun with(effect: () -> H): O =
        with(effect())

    suspend infix fun with(effect: H): O =
        run { block(effect) }

    suspend fun <A> Effect<A>.bind(): A =
        suspendCoroutine { cont -> this(cont) }

    companion object {
        fun <O, H : Handler> handle(block: suspend Effects<O, H>.(H) -> O) =
            Effects(block)
    }
}
