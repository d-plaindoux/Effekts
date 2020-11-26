package io.smallibs

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.Continuation
import kotlin.coroutines.suspendCoroutine
import kotlin.reflect.KClass

class Effects(var effects: List<Effect<*, *>>) {
    data class Effect<E : Any, O>(val klass: KClass<E>, val code: (E) -> (Continuation<O>) -> Unit) {
        fun apply(a: E, cont: Continuation<O>): Unit =
            code(a)(cont)
    }

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

    infix fun handle(block: suspend Effects.() -> Any): Job =
        GlobalScope.launch { this@Effects.block() }

    companion object {
        fun withEffects(block: Effects.() -> Effects): Effects = Effects(listOf()).block()
    }
}
