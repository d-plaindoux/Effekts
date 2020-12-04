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
            val unit: Unit = set("World!").bind()
            val name: String = get<String>().bind()
            set("Hello $name").bind()
        } with {
            when (it) {
                is set<String> -> {
                    store.getAndSet(it.value)
                    it.resume(Unit)
                }
                is get<*> -> {
                    it as State<String, String> // WIP -> Should be reviewed
                    it.resume(store.value)
                }
            }
        }

        Await() atMost 5000 until { store.value == "Hello World!" }
    }
}
