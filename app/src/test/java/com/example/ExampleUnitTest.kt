package com.example

import com.example.data.NexfarRepository
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun testDropsCalculation() {
        val (macro, micro) = NexfarRepository.calculateDropsInfusion(500.0, 8.0)
        // 500 / (8 * 3) = 500 / 24 = 20.833
        assertEquals(20.83, macro, 0.1)
        // 500 / 8 = 62.5
        assertEquals(62.5, micro, 0.1)
    }

    @Test
    fun testDilutionCalculation() {
        // C1 * V1 = C2 * V2 -> 10% * 50mL = 2% * V2 -> V2 = 250mL
        val v2 = NexfarRepository.calculateDilution(10.0, 50.0, 2.0, 0.0)
        assertNotNull(v2)
        assertEquals(250.0, v2!!, 0.01)
    }

    @Test
    fun testPediatricDoseCalculation() {
        // Weight: 20kg, 50mg/kg/day = 1000mg/day
        // 3 takes/day = 333.33mg/take
        // Syrup conc: 50mg/mL -> 6.66mL/take
        val (daily, perTake, vol) = NexfarRepository.calculatePediatricDose(20.0, 50.0, 50.0, 3)
        assertEquals(1000.0, daily, 0.1)
        assertEquals(333.33, perTake, 0.1)
        assertEquals(6.66, vol, 0.1)
    }

    @Test
    fun testCreatinineClearanceCalculation() {
        // Male, 60yo, 70kg, Cr: 1.0 mg/dL
        // [(140 - 60) * 70] / (72 * 1.0) = 5600 / 72 = 77.77 mL/min
        val clcr = NexfarRepository.calculateCreatinineClearance(60, 70.0, 1.0, false)
        assertEquals(77.77, clcr, 0.1)
    }
}
