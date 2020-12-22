package io.smallibs.effekts

import io.smallibs.control.Monad.Companion.fluent
import io.smallibs.data.Effect
import io.smallibs.data.EffectMonad
import io.smallibs.effekts.core.Effects.Companion.handle
import io.smallibs.effekts.stdlib.StateEff
import kotlinx.atomicfu.AtomicRef
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.test.Test
import kotlin.test.assertEquals

class StateTest {

    @ExperimentalCoroutinesApi
    @Test
    fun shouldPerformEffect() {
        val store: AtomicRef<String> = atomic("")

        val handled = handle<Unit, StateEff<String>> { state ->
            EffectMonad().fluent {
                state.set("World!") bind {
                    state.get
                } bind { name ->
                    state.set("Hello $name")
                }
            }.perform()
        } with StateEff(
            get = Effect { k ->
                k(store.value)
            },
            set = { value ->
                Effect { k ->
                    store.getAndSet(value)
                    k(Unit)
                }
            }
        )

        handled.unsafeRun()

        assertEquals(store.value, "Hello World!")
    }
}
