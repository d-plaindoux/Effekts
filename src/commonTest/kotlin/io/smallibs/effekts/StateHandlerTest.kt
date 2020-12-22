package io.smallibs.effekts

class StateHandlerTest {
/*
    @ExperimentalCoroutinesApi
    @Test
    fun shouldPerformEffect() {
        val handledEffects = handle<State<Unit, Int>, StateHandler<Int>> { state ->
            EffectMonad().fluent {
                StateMonad<Int>().fluent {
                    state.get map { s ->
                        s.fix().get
                    } bind {

                    }
                }
            }.perform().fix()(42).first.perform().fix() // Ugly for the moment
        } with StateHandler(
            set = { value ->
                Effect { k ->
                    k(State { Unit to value })
                }
            },
            get = Effect { k ->
                k(State { s: Int -> s to s })
            }
        )

        val unsafeRun: Pair<Unit, Int> = handledEffects.unsafeRun()(40)

        assertEquals(unsafeRun.second, 42)
    }
 */
}
