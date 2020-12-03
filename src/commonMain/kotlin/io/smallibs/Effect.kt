package io.smallibs

import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

open class Effect<T>(
    val id: suspend (T) -> T = { v ->
        suspendCoroutine { cont -> cont.resume(v) }
    }
)

