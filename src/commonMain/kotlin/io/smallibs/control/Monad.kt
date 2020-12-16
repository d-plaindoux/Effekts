package io.smallibs.control

class FluentMonad<F>(private val monad: Monad<F>) : FluentApplicative<F>(monad.applicative) {
    fun <A> App<F, App<F, A>>.join(): App<F, A> = monad.join(this)

    infix fun <A, B> App<F, A>.bind(f: (A) -> App<F, B>): App<F, B> = monad.bind(this, f)

    fun <A> returns(a: A): App<F, A> = monad.returns(a)
}

interface Monad<F> {
    val applicative: Applicative<F>

    fun <A> join(ma: App<F, App<F, A>>): App<F, A>

    fun <A, B> bind(ma: App<F, A>, f: (A) -> App<F, B>): App<F, B> = join(applicative.map(ma, f))

    fun <A> returns(a: A): App<F, A> = applicative.pure(a)

    companion object {
        suspend fun <F, R> Monad<F>.fluent(block: suspend FluentMonad<F>.() -> R): R =
            FluentMonad(this).block()
    }
}
