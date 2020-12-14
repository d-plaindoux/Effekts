package io.smallibs.effects

import io.smallibs.core.Effects.Companion.handle
import io.smallibs.utils.Await
import kotlin.test.Test
import kotlin.test.assertEquals

class IOConsoleTest {

    @Test
    fun shouldPerformEffect() {
        val actions = mutableListOf<String>()

        handle<Unit, IOConsole> { console ->
            val name = console.readString.bind()
            console.printString("Hello $name").bind()
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


        Await() atMost 5000 until { actions.size == 2 }

        assertEquals(
            listOf("readStream(World)", "printString(Hello World!)"),
            actions
        )
    }
}
