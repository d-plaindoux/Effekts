package io.smallibs.core

data class Effect<T>(val resume: suspend (suspend (T) -> T) -> T)