package io.smallibs.effects

import io.smallibs.core.And
import io.smallibs.core.And.Companion.and
import io.smallibs.core.Effects.Companion.handle
import io.smallibs.utils.Await
import kotlinx.atomicfu.AtomicRef
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlin.test.Test

class StateAndLogTest {

    @Test
    fun shouldPerformEffect() {
        val log: AtomicRef<String> = atomic("")
        val state = atomic(10)

        GlobalScope.async {
            handle<Unit, And<State<Int>, Log>> {
                val value1 = it.left.get.bind()
                it.left.set(value1 + 32).bind()
                val value2 = it.left.get.bind()
                it.right.log("Done with $value2").bind()
            } with {
                State<Int>(
                    set = { value ->
                        { k ->
                            state.value = value
                            k(Unit)
                        }
                    },
                    get = { k ->
                        k(state.value)
                    }
                ) and Log { value ->
                    { k ->
                        log.getAndSet(log.value + value)
                        k(Unit)
                    }
                }
            }
        }

        Await() atMost 5000 until { log.value == "Done with 42" }
    }
}
