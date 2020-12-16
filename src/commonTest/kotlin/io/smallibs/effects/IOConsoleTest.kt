package io.smallibs.effects

import io.smallibs.effect.Effects.Companion.handle
import io.smallibs.utils.Await
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlin.coroutines.resume
import kotlin.test.Test
import kotlin.test.assertEquals

class IOConsoleTest {

    @Test
    fun shouldPerformEffect() {
        val actions = mutableListOf<String>()

        GlobalScope.async {
            handle<Unit, IOConsole> { console ->
                val name = console.readString.perform()
                console.printString("Hello $name").perform()
            } with IOConsole(
                printString = { text ->
                    { k ->
                        actions += "printString($text)"
                        k.resume(Unit)
                    }
                },
                readString = { k ->
                    actions += "readStream(World)"
                    k.resume("World!")
                }
            )
        }

        Await() atMost 5000 until { actions.size == 2 }

        assertEquals(
            listOf("readStream(World)", "printString(Hello World!)"),
            actions
        )
    }
}
