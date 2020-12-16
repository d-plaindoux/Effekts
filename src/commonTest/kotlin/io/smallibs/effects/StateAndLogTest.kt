package io.smallibs.effects

import io.smallibs.effect.And
import io.smallibs.effect.And.Companion.and
import io.smallibs.effect.Effects.Companion.handle
import io.smallibs.utils.Await
import kotlinx.atomicfu.AtomicRef
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlin.coroutines.resume
import kotlin.test.Test

class StateAndLogTest {

    @Test
    fun shouldPerformEffect() {
        val log: AtomicRef<String> = atomic("")
        val state = atomic(10)

        GlobalScope.async {
            handle<Unit, And<State<Int>, Log>> {
                val value1 = it.left.get.perform()
                it.left.set(value1 + 32).perform()
                val value2 = it.left.get.perform()
                it.right.log("Done with $value2").perform()
            } with {
                State<Int>(
                    set = { value ->
                        { k ->
                            state.value = value
                            k.resume(Unit)
                        }
                    },
                    get = { k ->
                        k.resume(state.value)
                    }
                ) and Log { value ->
                    { k ->
                        log.getAndSet(log.value + value)
                        k.resume(Unit)
                    }
                }
            }
        }

        Await() atMost 5000 until { log.value == "Done with 42" }
    }
}
