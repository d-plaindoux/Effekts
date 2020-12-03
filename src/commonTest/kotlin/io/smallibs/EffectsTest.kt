package io.smallibs

import io.smallibs.Effects.Companion.handle
import io.smallibs.EffectsTest.IOConsole.printString
import io.smallibs.EffectsTest.IOConsole.readString
import io.smallibs.utils.Await
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.test.Test
import kotlin.test.assertEquals

class EffectsTest {

    // May be this can be automatically generated ?
    sealed class IOConsole<T>(val id: suspend (T) -> T) {
        data class printString(val text: String) : IOConsole<Unit>({ v -> suspendCoroutine { cont -> cont.resume(v) } })
        class readString() : IOConsole<String>({ v -> suspendCoroutine { cont -> cont.resume(v) } })
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
                    it.id(Unit)
                }
                is readString -> {
                    actions += "readStream(World)"
                    it.id("World")
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
