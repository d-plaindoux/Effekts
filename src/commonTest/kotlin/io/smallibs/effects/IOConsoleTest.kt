package io.smallibs.effects

import io.smallibs.core.Handler.Companion.handle
import io.smallibs.effects.IOConsole.printString
import io.smallibs.effects.IOConsole.readString
import io.smallibs.utils.Await
import kotlin.test.Test
import kotlin.test.assertEquals

class IOConsoleTest {

    @Test
    fun shouldPerformEffect() {
        val actions = mutableListOf<String>()

        handle<Unit, IOConsole<*>> {
            val name = readString().bind()
            printString("Hello $name").bind()
        } with { v, k ->
            when (v) {
                is printString -> {
                    actions += "printString(" + v.text + ")"
                    k(Unit)
                }
                is readString -> {
                    actions += "readStream(World)"
                    k("World")
                }
            }
        }

        Await() atMost 5000 until { actions.size == 2 }

        assertEquals(
            listOf("readStream(World)", "printString(Hello World)"),
            actions
        )
    }
}
