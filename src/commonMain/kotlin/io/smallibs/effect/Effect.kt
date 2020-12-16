package io.smallibs.effect

import kotlin.coroutines.Continuation

typealias Effect<T> = (Continuation<T>) -> Unit

