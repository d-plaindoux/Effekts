package io.smallibs.continuation.thermometre

import io.smallibs.continuation.thermometer.Thermometer
import kotlin.test.Test
import kotlin.test.assertEquals

class ThermometerTest {

    @Test
    internal fun shouldApplyThermometer() {
        val result = Thermometer.run<Int> {
            reset {
                val x: Int = shift { k ->
                    k(5) + k(5)
                }
                x + 1
            }
        }

        assertEquals(12, result)
    }
}
