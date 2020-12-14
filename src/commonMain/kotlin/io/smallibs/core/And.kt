package io.smallibs.core

class And<L, R>(val left: L, val right: R) {
    companion object {
        infix fun <L, R> L.and(right: R) = And(this, right)
    }
}
