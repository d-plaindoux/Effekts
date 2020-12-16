package io.smallibs.data

import io.smallibs.control.App
import io.smallibs.control.Applicative
import io.smallibs.control.Functor
import io.smallibs.control.Monad
import io.smallibs.control.Monad.Companion.fluent
import io.smallibs.data.StateK.Companion.fix
import io.smallibs.data.StateK.Companion.unfix

typealias State<A, S> = (S) -> Pair<A, S>

class StateK<S> private constructor() {
    @Suppress("UNCHECKED_CAST")
    companion object {
        fun <A, S> App<StateK<S>, A>.fix(): State<A, S> =
            this as State<A, S>

        fun <A, S> State<A, S>.unfix(): App<StateK<S>, A> =
            this as App<StateK<S>, A>
    }
}

class StateFunctor<S> : Functor<StateK<S>> {
    override fun <A, B> map(ma: App<StateK<S>, A>, f: (A) -> B): App<StateK<S>, B> = { s: S ->
        val (a, sp) = ma.fix()(s)
        f(a) to sp
    }.unfix()
}

class StateApplicative<S>(override val functor: Functor<StateK<S>> = StateFunctor()) : Applicative<StateK<S>>,
    Functor<StateK<S>> by functor {
    override fun <A> pure(a: A): App<StateK<S>, A> = { s: S -> (a to s) }.unfix()
    override fun <A, B> apply(mf: App<StateK<S>, (A) -> B>): (App<StateK<S>, A>) -> App<StateK<S>, B> =
        { ma ->
            { s: S ->
                val (f, sp) = mf.fix()(s)
                val (a, spp) = ma.fix()(sp)
                f(a) to spp
            }.unfix()
        }
}

class StateMonad<S>(override val applicative: Applicative<StateK<S>> = StateApplicative()) : Monad<StateK<S>>,
    Applicative<StateK<S>> by applicative {
    override fun <A> join(mma: App<StateK<S>, App<StateK<S>, A>>): App<StateK<S>, A> = { s: S ->
        val (ma, sp) = mma.fix()(s)
        val (a, spp) = ma.fix()(sp)
        a to spp
    }.unfix()
}

fun <S> get() = { s: S -> s to s }.unfix()
fun <S> set(ns: S) = { s: S -> Unit to ns }.unfix()
fun <S> modify(f: (S) -> S) = StateMonad<S>().fluent {
    get<S>() bind { s: S -> set(f(s)) }
}