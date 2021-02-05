package io.smallibs.effekts

import io.smallibs.control.Monad.Companion.fluent
import io.smallibs.data.Effect
import io.smallibs.data.EffectMonad
import io.smallibs.effekts.core.Effects.Companion.handle
import io.smallibs.effekts.stdlib.Log
import kotlinx.atomicfu.AtomicRef
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.test.Test
import kotlin.test.assertEquals

class LogTest {

    @ExperimentalCoroutinesApi
    @Test
    fun shouldPerformEffect() {
        val log: AtomicRef<String> = atomic("")

        val handled = handle<Unit, Log> { logger ->
            EffectMonad().fluent {
                logger.log("Hello ") bind {
                    logger.log("World!")
                }
            }.perform()
        } with Log { value ->
            Effect { k ->
                log.getAndSet(log.value + value)
                k(Unit)
            }
        }

        handled.unsafeSyncRun()

        assertEquals(log.value, "Hello World!")
    }
}
