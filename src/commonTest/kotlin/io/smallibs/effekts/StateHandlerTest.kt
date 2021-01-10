package io.smallibs.effekts

import io.smallibs.control.Monad.Companion.fluent
import io.smallibs.data.Effect
import io.smallibs.data.EffectMonad
import io.smallibs.data.State
import io.smallibs.data.StateK.Companion.invoke
import io.smallibs.data.StateMonad
import io.smallibs.effekts.core.Effects.Companion.handle
import io.smallibs.effekts.stdlib.StateHandler
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.test.Test
import kotlin.test.assertEquals

class StateHandlerTest {

    @ExperimentalCoroutinesApi
    @Test
    fun shouldPerformEffect() {
        val handledEffects =
            handle<Int, StateHandler<Int>> { state ->
                EffectMonad().fluent {
                    StateMonad<Int>().fluent {
                        state.get bind { s ->
                            state.set(s(42).first)
                        }
                    }
                }.perform()(0).second // Ugly for the moment
            } with StateHandler(
                set = { value ->
                    Effect { k ->
                        k(State { Unit to value })
                    }
                },
                get = Effect { k ->
                    k(State { s: Int -> s to s })
                }
            )

        val unsafeRun: Int = handledEffects.unsafeRun()

        assertEquals(unsafeRun, 42)
    }
}
