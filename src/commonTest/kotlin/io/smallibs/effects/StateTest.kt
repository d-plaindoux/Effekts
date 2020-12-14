package io.smallibs.effects

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
            val name: String = state.get.bind()
            state.set("Hello $name").bind()
        } with {
            State(
                { value ->
                    { k ->
                        store.getAndSet(value)
                        k(Unit)
                    }
                },
                { k ->
                    k(store.value)
                }
            )
        }

        Await() atMost 5000 until { store.value == "Hello World!" }
    }
}
