package io.smallibs

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlin.coroutines.Continuation
import kotlin.coroutines.suspendCoroutine

class Effects<E : Any, O>(var effects: List<Effect<*, *>>) {

    inline infix fun <reified I : E, R> effect(noinline code: (I) -> (Continuation<R>) -> Unit): Effects<E, O> {
        effects += Effect(I::class, code)
        return this
    }

    @Suppress("UNCHECKED_CAST")
    suspend fun <E : Any, O> perform(action: E): O = suspendCoroutine { cont ->
        val result =
            effects.filter { it.klass.isInstance(action) }
                .map { it as Effect<E, O> }
                .map { it.apply(action, cont) }
                .firstOrNull()

        if (result == null) {
            // TODO
        }
    }

    private fun handleWithEffects(block: suspend Effects<E, O>.() -> O): Deferred<O> =
        GlobalScope.async { this@Effects.block() }

    class Handler<E : Any, O>(private val code: suspend Effects<E, O>.() -> O) {
        infix fun with(effects: Effects<E, O>.() -> Effects<E, O>): Deferred<O> =
            Effects<E, O>(listOf()).effects().handleWithEffects(code)
    }

    companion object {
        fun <E : Any, O> handle(block: suspend Effects<E, O>.() -> O): Handler<E, O> = Handler<E, O>(block)
    }
}

