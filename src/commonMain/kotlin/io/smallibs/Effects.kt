package io.smallibs

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.Continuation
import kotlin.coroutines.suspendCoroutine

class Effects(var effects: List<Effect<*, *>>) {

    inline infix fun <reified E : Any, O> effect(noinline code: (E) -> (Continuation<O>) -> Unit): Effects {
        effects += Effect(E::class, code)
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

    private fun handleWithEffects(block: suspend Effects.() -> Any): Job =
        GlobalScope.launch { this@Effects.block() }

    class EffectsHandler(private val code: suspend Effects.() -> Any) {
        infix fun with(effects: Effects.() -> Effects): Job =
            Effects(listOf()).effects().handleWithEffects(code)
    }

    companion object {
        fun handle(block: suspend Effects.() -> Any): EffectsHandler = EffectsHandler(block)
    }
}
