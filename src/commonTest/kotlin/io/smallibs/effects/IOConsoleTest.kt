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
            val name: String = readString().bind()
            printString("Hello $name").bind()
        } with {
            when (it) {
                is printString -> {
                    actions += "printString(" + it.text + ")"
                    it.resume(Unit)
                }
                is readString -> {
                    actions += "readStream(World)"
                    it.resume("World")
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
