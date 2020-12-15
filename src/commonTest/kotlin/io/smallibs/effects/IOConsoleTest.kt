package io.smallibs.effects

import io.smallibs.core.Effects.Companion.handle
import io.smallibs.utils.Await
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlin.test.Test
import kotlin.test.assertEquals

class IOConsoleTest {

    @Test
    fun shouldPerformEffect() {
        val actions = mutableListOf<String>()

        GlobalScope.async {
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
        }

        Await() atMost 5000 until { actions.size == 2 }

        assertEquals(
            listOf("readStream(World)", "printString(Hello World!)"),
            actions
        )
    }
}
