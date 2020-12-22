package io.smallibs.extension

infix fun <A, B, C> ((A) -> B).then(f: (B) -> C): (A) -> C = {
    f(this(it))
}

infix fun <A, B, C> ((B) -> C).after(g: (A) -> B): (A) -> C = {
    this(g(it))
}
