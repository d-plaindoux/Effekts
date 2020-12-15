package io.smallibs.core

import kotlin.coroutines.Continuation

typealias Effect<T> = (Continuation<T>) -> Unit
