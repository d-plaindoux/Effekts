package io.smallibs

import io.smallibs.Effects.Companion.withEffects
import io.smallibs.utils.Await
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.test.Test
import kotlin.test.assertEquals

class EffectsTest {

    data class printString(val text: String)
    object readString

    @Test
    fun shouldPerformEffect() {
        val actions = mutableListOf<String>()

        withEffects {
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
        } handle {
            val name: String = perform(readString)
            perform(printString("Hello $name"))
        }

        Await() atMost 5000 until { actions.size == 2 }

        assertEquals(
            listOf("readStream(World)", "printString(Hello World)"),
            actions
        )
    }
}
