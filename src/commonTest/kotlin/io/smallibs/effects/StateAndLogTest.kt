package io.smallibs.effects

import io.smallibs.control.Monad.Companion.fluent
import io.smallibs.data.Effect
import io.smallibs.data.EffectMonad
import io.smallibs.data.EffetK.Companion.fix
import io.smallibs.effect.And
import io.smallibs.effect.And.Companion.and
import io.smallibs.effect.Effects.Companion.handle
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

        GlobalScope.async {
            handle<Unit, And<StateEff<Int>, Log>> { effect ->
                val state = effect.left
                val logger = effect.right

                EffectMonad().fluent {
                    state.get bind {
                        state.set(it + 32)
                    } bind {
                        state.get
                    } bind {
                        logger.log("Done with $it")
                    }
                }.fix().perform()
            } with {
                val state = atomic(10)

                StateEff<Int>(
                    set = { value ->
                        Effect { k ->
                            state.value = value
                            k(Unit)
                        }
                    },
                    get = Effect { k ->
                        k(state.value)
                    }
                ) and Log { value ->
                    Effect { k ->
                        log.getAndSet(log.value + value)
                        k(Unit)
                    }
                }
            }
        }

        Await() atMost 5000 until { log.value == "Done with 42" }
    }
}
