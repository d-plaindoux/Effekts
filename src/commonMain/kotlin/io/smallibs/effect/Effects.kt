package io.smallibs.effect

import kotlin.coroutines.suspendCoroutine

class Effects<O, H : Handler>(private val block: suspend Effects<O, H>.(H) -> O) {

    suspend infix fun with(effect: () -> H): O =
        with(effect())

    suspend infix fun with(effect: H): O =
        block(effect)

    suspend fun <A> Effect<A>.perform(): A =
        suspendCoroutine { cont -> this(cont) }

    suspend fun <A> perform(e: Effect<A>): A =
        suspendCoroutine { cont -> e(cont) }

    companion object {
        fun <O, H : Handler> handle(block: suspend Effects<O, H>.(H) -> O) =
            Effects(block)
    }
}
