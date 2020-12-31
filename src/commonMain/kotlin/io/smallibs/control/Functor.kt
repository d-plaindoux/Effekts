package io.smallibs.control

interface Functor<F> {
    fun <A, B> map(ma: App<F, A>, f: (A) -> B): App<F, B>

    open class FluentFunctor<F>(private val functor: Functor<F>) {
        infix fun <A, B> App<F, A>.map(f: (A) -> B): App<F, B> = functor.map(this, f)
    }

    companion object {
        fun <F, R> Functor<F>.fluent(block: FluentFunctor<F>.() -> R): R =
            FluentFunctor(this).block()
    }
}
