package io.smallibs.effects

import io.smallibs.control.App
import io.smallibs.control.Monad.Companion.fluent
import io.smallibs.data.*
import io.smallibs.data.EffetK.Companion.fix
import io.smallibs.data.StateK.Companion.fix
import io.smallibs.effect.Effects.Companion.handle
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.test.Test

class StateHandlerTest {

/*
    @ExperimentalCoroutinesApi
    @Test
    fun shouldPerformEffect() {
        val state =
            handle<App<StateK<Int>, Unit>, StateHandler<Int>> { state ->
                StateMonad<Int>().fluent {
                    EffectMonad().fluent {
                        val app: App<EffetK, App<StateK<Int>, Effect<App<StateK<Int>, Unit>>>> = state.get map { s: App<StateK<Int>, Int> -> s map { state.set(it + 1) } }
                        app
                    }
                }.fix().perform().fix()(41).first.perform()
            } with StateHandler(
                set = { value ->
                    Effect { k ->
                        k(State { s: Int -> Unit to value })
                    }
                },
                get = Effect { k ->
                    k(State { s: Int -> s to s })
                }
            )
    }

    Await() atMost 5000 until { state.fix()(41).second == 42 }
}

 */
}


