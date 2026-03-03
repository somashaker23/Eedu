package com.eedu.gifttracker.util

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ValidationTest {

    @Test
    fun `empty name is invalid`() {
        assertFalse(isValidGiverName(""))
        assertFalse(isValidGiverName("   "))
    }

    @Test
    fun `non-empty name is valid`() {
        assertTrue(isValidGiverName("Rama Rao"))
        assertTrue(isValidGiverName("A"))
    }

    @Test
    fun `zero amount is invalid`() {
        assertFalse(isValidAmount(0.0))
        assertFalse(isValidAmount(-100.0))
    }

    @Test
    fun `positive amount is valid`() {
        assertTrue(isValidAmount(1.0))
        assertTrue(isValidAmount(500.0))
        assertTrue(isValidAmount(0.01))
    }

    private fun isValidGiverName(name: String): Boolean = name.trim().isNotEmpty()
    private fun isValidAmount(amount: Double): Boolean = amount > 0
}
