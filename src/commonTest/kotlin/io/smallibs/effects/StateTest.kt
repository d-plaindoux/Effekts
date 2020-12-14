package io.smallibs.effects

import io.smallibs.core.Effect
import io.smallibs.core.Effects.Companion.handle
import io.smallibs.utils.Await
import kotlinx.atomicfu.AtomicRef
import kotlinx.atomicfu.atomic
import kotlin.test.Test

class StateTest {

    @Test
    fun shouldPerformEffect() {
        val store: AtomicRef<String> = atomic("")

        handle<Unit, State<String>> { state ->
            state.set("World!").bind()
            val name: String = state.get().bind()
            state.set("Hello $name").bind()
        } with {
            object : State<String> {
                override fun set(value: String): Effect<Unit> = { k ->
                    store.getAndSet(value)
                    k(Unit)
                }

                override fun get(): Effect<String> = { k ->
                    k(store.value)
                }
            }
        }

        Await() atMost 5000 until { store.value == "Hello World!" }
    }
}
