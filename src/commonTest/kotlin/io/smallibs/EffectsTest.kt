package io.smallibs

import io.smallibs.EffectsTest.IOConsole.printString
import io.smallibs.EffectsTest.IOConsole.readString
import io.smallibs.Handler.Companion.handle
import io.smallibs.utils.Await
import kotlin.test.Test
import kotlin.test.assertEquals

class EffectsTest {

    // Effect definition
    sealed class IOConsole<T> : Effect<T>() {
        data class printString(val text: String) : IOConsole<Unit>()
        class readString : IOConsole<String>()
    }

    @Test
    fun shouldPerformEffect() {
        val actions = mutableListOf<String>()

        handle<Unit, IOConsole<*>> {
            val name: String = perform(readString())
            perform(printString("Hello $name"))
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
