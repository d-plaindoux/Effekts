package io.smallibs

import kotlin.coroutines.Continuation
import kotlin.reflect.KClass

class Effect<E : Any, O>(val klass: KClass<E>, private val code: (E) -> (Continuation<O>) -> Unit) {
    fun apply(a: E, cont: Continuation<O>): Unit = code(a)(cont)
}
