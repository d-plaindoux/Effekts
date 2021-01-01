package io.smallibs.data

import io.smallibs.control.App
import io.smallibs.control.Applicative
import io.smallibs.control.Functor
import io.smallibs.control.Monad
import io.smallibs.control.Monad.Companion.fluent
import io.smallibs.data.StateK.Companion.invoke

class State<A, S>(private val state: (S) -> Pair<A, S>) : (S) -> Pair<A, S>, App<StateK<S>, A> {
    override operator fun invoke(s: S): Pair<A, S> = state(s)
}

class StateK<S> private constructor() {
    @Suppress("UNCHECKED_CAST")
    companion object {
        fun <A, S> App<StateK<S>, A>.fix(): State<A, S> =
            this as State<A, S>

        operator fun <A, S> App<StateK<S>, A>.invoke(s: S): Pair<A, S> =
            this.fix()(s)
    }
}

class StateFunctor<S> : Functor<StateK<S>> {
    override fun <A, B> map(ma: App<StateK<S>, A>, f: (A) -> B): App<StateK<S>, B> = State { s: S ->
        val (a, sp) = ma(s)
        f(a) to sp
    }
}

class StateApplicative<S>(override val functor: Functor<StateK<S>> = StateFunctor()) :
    Applicative<StateK<S>>,
    Functor<StateK<S>> by functor {
    override fun <A> pure(a: A): App<StateK<S>, A> = State { s: S -> (a to s) }

    override fun <A, B> apply(mf: App<StateK<S>, (A) -> B>): (App<StateK<S>, A>) -> App<StateK<S>, B> =
        { ma ->
            State { s: S ->
                val (f, sp) = mf(s)
                val (a, spp) = ma(sp)
                f(a) to spp
            }
        }

    override fun <A, B> map(ma: App<StateK<S>, A>, f: (A) -> B): App<StateK<S>, B> {
        return functor.map(ma, f)
    }
}

class StateMonad<S>(override val applicative: Applicative<StateK<S>> = StateApplicative()) :
    Monad<StateK<S>>,
    Applicative<StateK<S>> by applicative {
    override fun <A> join(mma: App<StateK<S>, App<StateK<S>, A>>): App<StateK<S>, A> = State { s ->
        val (ma, sp) = mma(s)
        val (a, spp) = ma(sp)
        a to spp
    }
}

fun <S> get() = State { s: S -> s to s }
fun <S> set(ns: S) = State { _: S -> Unit to ns }
fun <S> modify(f: (S) -> S) = StateMonad<S>().fluent {
    get<S>() bind { s: S -> set(f(s)) }
}
