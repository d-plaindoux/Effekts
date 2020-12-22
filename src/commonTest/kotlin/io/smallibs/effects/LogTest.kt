package io.smallibs.effects

import io.smallibs.control.Monad.Companion.fluent
import io.smallibs.data.Effect
import io.smallibs.data.EffectMonad
import io.smallibs.effect.Effects.Companion.handle
import io.smallibs.utils.Await
import kotlinx.atomicfu.AtomicRef
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlin.test.Test

class LogTest {

    @Test
    fun shouldPerformEffect() {
        val log: AtomicRef<String> = atomic("")

        GlobalScope.async {
            handle<Unit, Log> { logger ->
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
        }

        Await() atMost 5000 until { log.value == "Hello World!" }
    }
}
