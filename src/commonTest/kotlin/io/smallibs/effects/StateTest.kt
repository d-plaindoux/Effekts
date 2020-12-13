package io.smallibs.effects

import io.smallibs.core.Handler.Companion.handle
import io.smallibs.effects.State.get
import io.smallibs.effects.State.set
import io.smallibs.utils.Await
import kotlinx.atomicfu.AtomicRef
import kotlinx.atomicfu.atomic
import kotlin.test.Test

class StateTest {

    @Test
    fun shouldPerformEffect() {
        val store: AtomicRef<String> = atomic("")

        handle<Unit, State<String, *>> {
            set("World!").bind()
            val name: String = get<String>().bind()
            set("Hello $name").bind()
        } with { v, k ->
            when (v) {
                is set -> {
                    store.getAndSet(v.value)
                    k(Unit)
                }
                is get<*> -> {
                    v as get<String> // WIP -> Should be reviewed
                    k(store.value)
                }
            }
        }


        Await() atMost 5000 until { store.value == "Hello World!" }
    }
}
