package io.smallibs.effekts.core

class And<L : Handler, R : Handler>(val left: L, val right: R) : Handler {
    companion object {
        infix fun <L : Handler, R : Handler> L.and(right: R) = And(this, right)
    }
}
