package io.smallibs.effekts.core

import io.smallibs.control.App
import io.smallibs.data.Effect
import io.smallibs.data.EffetK
import io.smallibs.data.EffetK.Companion.fix
import io.smallibs.data.Internal
import kotlin.coroutines.suspendCoroutine

class Effects<O, H : Handler>(private val block: suspend Effects<O, H>.(H) -> O) {

    infix fun with(effect: () -> H): HandledEffects<O> =
        with(effect())

    infix fun with(effect: H): HandledEffects<O> =
        HandledEffects { block(effect) }

    suspend fun <A> Effect<A>.perform(): A =
        suspendCoroutine { this(Internal(it).reify()) }

    suspend fun <A> App<EffetK, A>.perform(): A =
        this.fix().perform()

    companion object {
        fun <O, H : Handler> handle(block: suspend Effects<O, H>.(H) -> O): Effects<O, H> =
            Effects(block)
    }
}
