package io.smallibs.effects

import io.smallibs.core.Handler.Companion.handle
import io.smallibs.effects.Log.log
import io.smallibs.utils.Await
import kotlinx.atomicfu.AtomicRef
import kotlinx.atomicfu.atomic
import kotlin.test.Test

class LogTest {

    @Test
    fun shouldPerformEffect() {
        val log: AtomicRef<String> = atomic("")

        handle<Unit, Log<*>> {
            log("Hello ").bind()
            log("World!").bind()
        } with { v, k ->
            when (v) {
                is log -> {
                    log.getAndSet(log.value + v.value)
                    k(Unit)
                }
            }
        }


        Await() atMost 5000 until { log.value == "Hello World!" }
    }
}
