package io.smallibs.effects

import io.smallibs.core.Effects.Companion.handle
import io.smallibs.utils.Await
import kotlinx.atomicfu.AtomicRef
import kotlinx.atomicfu.atomic
import kotlin.test.Test

class LogTest {

    @Test
    fun shouldPerformEffect() {
        val log: AtomicRef<String> = atomic("")

        handle<Unit, Log> { logger ->
            logger.log("Hello ").bind()
            logger.log("World!").bind()
        } with Log { value ->
            { k ->
                log.getAndSet(log.value + value)
                k(Unit)
            }
        }


        Await() atMost 5000 until { log.value == "Hello World!" }
    }
}
