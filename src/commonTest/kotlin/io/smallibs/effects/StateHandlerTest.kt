package io.smallibs.effects

import io.smallibs.control.App
import io.smallibs.control.Monad.Companion.fluent
import io.smallibs.data.StateK
import io.smallibs.data.StateK.Companion.fix
import io.smallibs.data.StateK.Companion.unfix
import io.smallibs.data.StateMonad
import io.smallibs.effect.Effects.Companion.handle
import io.smallibs.utils.Await
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.coroutines.resume
import kotlin.test.Test

class StateHandlerTest {

    private fun <T> waitFor(f: suspend () -> T): T = TODO() // Un truc à la Arrow.Fx !

    @ExperimentalCoroutinesApi
    @Test
    fun shouldPerformEffect() {
        val state = waitFor {
            handle<App<StateK<Int>, Unit>, StateHandler<Int>> { state ->
                StateMonad<Int>().fluent {
                    state.get.perform() bind { s ->
                        waitFor { state.set(s + 1).perform() } // waitFor o_O
                    }
                }
            } with StateHandler(
                set = { value ->
                    { k ->
                        k.resume({ s: Int -> Unit to value }.unfix())
                    }
                },
                get = { k ->
                    k.resume({ s: Int -> s to s }.unfix())
                }
            )
        }

        Await() atMost 5000 until { state.fix()(41).second == 42 }
    }
}
