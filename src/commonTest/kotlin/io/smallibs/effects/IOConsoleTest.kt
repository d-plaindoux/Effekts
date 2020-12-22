package io.smallibs.effects

import io.smallibs.control.Monad.Companion.fluent
import io.smallibs.data.Effect
import io.smallibs.data.EffectMonad
import io.smallibs.effect.Effects.Companion.handle
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
        }

        Await() atMost 5000 until { actions.size == 2 }

        assertEquals(
            listOf("readStream(World)", "printString(Hello World!)"),
            actions
        )
    }
}
