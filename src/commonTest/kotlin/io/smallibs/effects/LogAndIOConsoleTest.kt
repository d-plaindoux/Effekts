package io.smallibs.effects

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

        handle<Unit, Log> { logger ->
            handle<Unit, IOConsole> { console ->
                val name = console.readString.bind()
                console.printString("Hello $name").bind()
                logger.log("Done").bind()
            } with IOConsole(
                { text ->
                    { k ->
                        actions += "printString($text)"
                        k(Unit)
                    }
                },
                { k ->
                    actions += "readStream(World)"
                    k("World!")
                }
            )

        } with Log { value ->
            { k ->
                log.getAndSet(log.value + value)
                k(Unit)
            }
        }


        Await() atMost 5000 until { log.value == "Done" }

        assertEquals(
            listOf("readStream(World)", "printString(Hello World!)"),
            actions
        )
    }
}
