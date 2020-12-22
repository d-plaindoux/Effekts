package io.smallibs.stdlib

infix fun <A, B, C> ((A) -> B).then(f: (B) -> C): (A) -> C =
    { a -> f(this(a)) }

infix fun <A, B, C> ((B) -> C).after(g: (A) -> B): (A) -> C =
    { a -> this(g(a)) }