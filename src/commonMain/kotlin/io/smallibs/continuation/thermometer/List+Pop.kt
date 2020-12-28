package io.smallibs.continuation.thermometer

internal fun <A> List<A>.pop(): Pair<A, List<A>> =
    this[0] to this.subList(1, this.size)

internal fun <E> List<E>.pop(d: E): Pair<E, List<E>> =
    if (this.isEmpty()) {
        d to this
    } else {
        this.pop()
    }
