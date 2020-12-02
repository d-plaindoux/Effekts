package io.smallibs

import io.smallibs.Effects.Companion.handle
import io.smallibs.utils.Await
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.test.Test
import kotlin.test.assertEquals

class EffectsTest {

    interface IOConsole
    data class printString(val text: String) : IOConsole
    object readString : IOConsole

    @Test
    fun shouldPerformEffect() {
        val actions = mutableListOf<String>()

        handle<IOConsole, Unit> {
            val name: String = perform(readString)
            perform(printString("Hello $name"))
        } with {
            effect { p: printString ->
                { k: Continuation<Unit> ->
                    actions += "printString(" + p.text + ")"
                    k.resume(Unit)
                }
            }
            effect { _: readString ->
                { k: Continuation<String> ->
                    actions += "readStream(World)"
                    k.resume("World")
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
