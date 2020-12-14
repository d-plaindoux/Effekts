package io.smallibs.core

typealias Effect<T> = suspend (suspend (T) -> T) -> T
