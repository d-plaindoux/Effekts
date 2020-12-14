package io.smallibs.effects

import io.smallibs.core.And
import io.smallibs.core.And.Companion.and
import io.smallibs.core.Effects.Companion.handle
import io.smallibs.utils.Await
import kotlinx.atomicfu.AtomicRef
import kotlinx.atomicfu.atomic
import kotlin.test.Test
import kotlin.test.assertEquals

class LogAndIOConsoleTest {

    @Test
    fun shouldPerformEffect() {
        val log: AtomicRef<String> = atomic("")
        val actions = mutableListOf<String>()

        handle<Unit, And<State<String>, Log>> {
            val name = it.left.get.bind()
            it.left.set("Hello $name").bind()
            it.right.log("Done").bind()
        } with {
            State<String>(
                set = { text ->
                    { k ->
                        actions += "printString($text)"
                        k(Unit)
                    }
                },
                get = { k ->
                    actions += "readStream(World)"
                    k("World!")
                }
            ) and Log { value ->
                { k ->
                    log.getAndSet(log.value + value)
                    k(Unit)
                }
            }
        }

        Await() atMost 5000 until { log.value == "Done" }

        assertEquals(
            listOf("readStream(World)", "printString(Hello World!)"),
            actions
        )
    }
}
