package io.smallibs.effekts

import io.smallibs.control.Monad.Companion.fluent
import io.smallibs.data.Effect
import io.smallibs.data.EffectMonad
import io.smallibs.effekts.core.Effects.Companion.handle
import io.smallibs.effekts.stdlib.IOConsole
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.test.Test
import kotlin.test.assertEquals

class IOConsoleTest {

    @ExperimentalCoroutinesApi
    @Test
    fun shouldPerformEffect() {
        val actions = mutableListOf<String>()

        val handled = handle<Unit, IOConsole> { console ->
            EffectMonad().fluent {
                console.readString bind {
                    console.printString("Hello $it")
                }
            }.perform()
        } with IOConsole(
            printString = { text ->
                Effect { k ->
                    actions += "printString($text)"
                    k(Unit)
                }
            },
            readString = Effect { k ->
                actions += "readStream(World)"
                k("World!")
            }
        )

        handled.unsafeRun()

        assertEquals(
            listOf("readStream(World)", "printString(Hello World!)"),
            actions
        )
    }
}
