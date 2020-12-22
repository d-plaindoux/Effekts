package io.smallibs.effects

class StateHandlerTest {
/*
    @Test
    fun shouldPerformEffect() {
        val state: Deferred<App<StateK<Int>, Effect<App<StateK<Int>, Unit>>>> =
            GlobalScope.async {
                handle<App<StateK<Int>, Effect<App<StateK<Int>, Unit>>>, StateHandler<Int>> { state ->
                    EffectMonad().fluent {
                        StateMonad<Int>().fluent {
                            val app: App<EffetK, App<StateK<Int>, Effect<App<StateK<Int>, Unit>>>> =
                                state.get map { s: App<StateK<Int>, Int> -> s map { state.set(it + 1) } }
                            app
                        }
                    }.fix().perform()
                } with StateHandler(
                    set = { value ->
                        Effect { k ->
                            k(State { s: Int -> Unit to value })
                        }
                    },
                    get = Effect { k ->
                        k(State { s: Int -> s to s })
                    }
                )
            }

        Await() atMost 5000 until { state.(41).second == 42 }

    }
 */
}
