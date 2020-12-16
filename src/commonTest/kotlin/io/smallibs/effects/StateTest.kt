package io.smallibs.effects

import io.smallibs.effect.Effects.Companion.handle
import io.smallibs.utils.Await
import kotlinx.atomicfu.AtomicRef
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlin.coroutines.resume
import kotlin.test.Test

class StateTest {

    @Test
    fun shouldPerformEffect() {
        val store: AtomicRef<String> = atomic("")

        GlobalScope.async {
            handle<Unit, State<String>> { state ->
                state.set("World!").perform()
                val name: String = state.get.perform()
                state.set("Hello $name").perform()
            } with State(
                get = { k ->
                    k.resume(store.value)
                },
                set = { value ->
                    { k ->
                        store.getAndSet(value)
                        k.resume(Unit)
                    }
                }
            )
        }

        Await() atMost 5000 until { store.value == "Hello World!" }
    }
}
