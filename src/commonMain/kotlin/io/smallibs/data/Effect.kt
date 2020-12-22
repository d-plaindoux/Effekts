package io.smallibs.data

import io.smallibs.control.App
import io.smallibs.control.Applicative
import io.smallibs.control.Functor
import io.smallibs.control.Monad
import io.smallibs.data.EffetK.Companion.invoke
import io.smallibs.stdlib.then
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume

data class Internal<A>(val cont: Continuation<A>) : (A) -> Unit {
    override fun invoke(a: A) = cont.resume(a)
}

data class Effect<A>(private val behavior: ((A) -> Unit) -> Unit) : App<EffetK, A>, ((A) -> Unit) -> Unit {
    override operator fun invoke(a: (A) -> Unit) = behavior(a)
}

class EffetK private constructor() {
    @Suppress("UNCHECKED_CAST")
    companion object {
        fun <A> App<EffetK, A>.fix(): Effect<A> =
            this as Effect<A>

        operator fun <A> App<EffetK, A>.invoke(a: (A) -> Unit): Unit =
            this.fix()(a)
    }
}

class EffectFunctor : Functor<EffetK> {
    override fun <A, B> map(ma: App<EffetK, A>, f: (A) -> B): App<EffetK, B> =
        Effect { b -> ma(f then b) }
}

class EffectApplicative(override val functor: Functor<EffetK> = EffectFunctor()) : Applicative<EffetK> {
    override fun <A> pure(a: A): App<EffetK, A> =
        Effect { k -> k(a) }

    override fun <A, B> apply(mf: App<EffetK, (A) -> B>): (App<EffetK, A>) -> App<EffetK, B> = { ma ->
        Effect { b -> mf { f -> ma(f then b) } }
    }
}

class EffectMonad(override val applicative: Applicative<EffetK> = EffectApplicative()) : Monad<EffetK> {
    override fun <A> join(mma: App<EffetK, App<EffetK, A>>): App<EffetK, A> =
        Effect { a -> mma { ma -> ma(a) } }
}
