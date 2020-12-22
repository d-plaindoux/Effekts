package io.smallibs.control

open class FluentFunctor<F>(private val functor: Functor<F>) {
    infix fun <A, B> App<F, A>.map(f: (A) -> B): App<F, B> = functor.map(this, f)
}

interface Functor<F> {
    fun <A, B> map(ma: App<F, A>, f: (A) -> B): App<F, B>

    companion object {
        fun <F, R> Functor<F>.fluent(block: FluentFunctor<F>.() -> R): R =
            FluentFunctor(this).block()
    }
}
