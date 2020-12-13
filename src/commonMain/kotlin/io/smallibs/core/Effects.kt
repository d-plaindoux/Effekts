package io.smallibs.core

import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

// O is not used (phantom type)
private typealias EffectHandler<E, A> = suspend (E, suspend (A) -> A) -> A

class Effects<O, E : Effect<*>>(var effect: EffectHandler<E, *>) {
    private fun <T> continuation(): suspend (T) -> T = { v ->
        suspendCoroutine { cont -> cont.resume(v) }
    }

    suspend fun <A> Effect<A>.bind(): A = this@Effects.effect(this as E, continuation<A>()) as A
}
