package io.smallibs.effect

import io.smallibs.control.App
import io.smallibs.data.Effect
import io.smallibs.data.EffetK
import io.smallibs.data.EffetK.Companion.fix
import io.smallibs.data.Internal
import kotlin.coroutines.suspendCoroutine

class Effects<O, H : Handler>(private val block: suspend Effects<O, H>.(H) -> O) {

    suspend infix fun with(effect: () -> H): O =
        with(effect())

    suspend infix fun with(effect: H): O =
        block(effect)

    suspend fun <A> Effect<A>.perform(): A =
        suspendCoroutine { cont -> this.behavior(Internal(cont)) }

    suspend fun <A> App<EffetK, A>.perform(): A =
        suspendCoroutine { cont -> this.fix().behavior(Internal(cont)) }

    companion object {
        fun <O, H : Handler> handle(block: suspend Effects<O, H>.(H) -> O) =
            Effects(block)
    }
}
