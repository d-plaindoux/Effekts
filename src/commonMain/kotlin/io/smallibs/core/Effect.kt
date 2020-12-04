package io.smallibs.core

import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

open class Effect<T>(
    val resume: suspend (T) -> T = { v ->
        suspendCoroutine { cont -> cont.resume(v) }
    }
)
