package io.smallibs.continuation

import kotlin.test.Test
import kotlin.test.assertEquals

class ThermometerTest {

    @Test
    internal fun should() {
        val result = Thermometer.run<Int> {
            1 + reset {
                val x1 = shift<Int> { k -> k(5) }
                val x2 = reset {
                    val x = shift<Int> { k -> k(5) + k(5) }
                    x + 1
                }

                x1 + x2
            }
        }

        assertEquals(result, 18)
    }
}
