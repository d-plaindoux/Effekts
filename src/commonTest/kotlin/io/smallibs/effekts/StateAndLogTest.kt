package io.smallibs.effekts

import io.smallibs.control.Monad.Companion.fluent
import io.smallibs.data.Effect
import io.smallibs.data.EffectMonad
import io.smallibs.effekts.core.And
import io.smallibs.effekts.core.And.Companion.and
import io.smallibs.effekts.core.Effects.Companion.handle
import io.smallibs.effekts.stdlib.Log
import io.smallibs.effekts.stdlib.StateEff
import kotlinx.atomicfu.AtomicRef
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.test.Test
import kotlin.test.assertEquals

class StateAndLogTest {

    @ExperimentalCoroutinesApi
    @Test
    fun shouldPerformEffect() {
        val log: AtomicRef<String> = atomic("")

        val handled = handle<Unit, And<StateEff<Int>, Log>> { effects ->
            val state = effects.left
            val logger = effects.right

            EffectMonad().fluent {
                state.get bind {
                    state.set(it + 32)
                } bind {
                    state.get
                } bind {
                    logger.log("Done with $it")
                }
            }.perform()
        } with {
            val state = atomic(10)

            StateEff<Int>(
                set = { value ->
                    Effect { k ->
                        state.value = value
                        k(Unit)
                    }
                },
                get = Effect { k ->
                    k(state.value)
                }
            ) and Log { value ->
                Effect { k ->
                    log.getAndSet(log.value + value)
                    k(Unit)
                }
            }
        }

        handled.unsafeSyncRun()

        assertEquals(log.value, "Done with 42")
    }
}
