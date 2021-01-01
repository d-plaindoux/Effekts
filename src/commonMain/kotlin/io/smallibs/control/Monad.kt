package io.smallibs.control

import io.smallibs.control.Applicative.FluentApplicative

interface Monad<F> {
    val applicative: Applicative<F>

    fun <A> join(mma: App<F, App<F, A>>): App<F, A>

    fun <A, B> bind(ma: App<F, A>, f: (A) -> App<F, B>): App<F, B> = join(applicative.map(ma, f))

    fun <A> returns(a: A): App<F, A> = applicative.pure(a)

    open class FluentMonad<F>(private val monad: Monad<F>) : FluentApplicative<F>(monad.applicative) {
        fun <A> App<F, App<F, A>>.join(): App<F, A> = monad.join(this)

        infix fun <A, B> App<F, A>.bind(f: (A) -> App<F, B>): App<F, B> = monad.bind(this, f)

        fun <A> returns(a: A): App<F, A> = monad.returns(a)
    }

    companion object {
        fun <F, R> Monad<F>.fluent(block: FluentMonad<F>.() -> R): R =
            FluentMonad(this).block()
    }
}
