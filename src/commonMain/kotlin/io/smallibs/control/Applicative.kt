package io.smallibs.control

import io.smallibs.control.Functor.FluentFunctor

// ----------------------------------------------------------------------------

interface Applicative<F> {
    val functor: Functor<F>

    fun <A> pure(a: A): App<F, A>

    fun <A, B> apply(mf: App<F, (A) -> B>): (App<F, A>) -> App<F, B>

    fun <A, B> map(ma: App<F, A>, f: (A) -> B): App<F, B> = apply(pure(f))(ma)

    open class FluentApplicative<F>(private val applicative: Applicative<F>) : FluentFunctor<F>(applicative.functor) {
        fun <A> pure(a: A): App<F, A> = applicative.pure(a)

        infix fun <A, B> App<F, (A) -> B>.apply(ma: App<F, A>): App<F, B> = applicative.apply(this)(ma)
    }

    companion object {
        fun <F, R> Applicative<F>.fluent(block: FluentApplicative<F>.() -> R): R =
            FluentApplicative(this).block()
    }
}
