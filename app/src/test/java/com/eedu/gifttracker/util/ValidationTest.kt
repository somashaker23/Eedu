package com.eedu.gifttracker.util

import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ValidationTest {

    @Test
    fun `empty name is invalid`() {
        assertFalse(GiftValidator.isValidGiverName(""))
        assertFalse(GiftValidator.isValidGiverName("   "))
    }

    @Test
    fun `non-empty name is valid`() {
        assertTrue(GiftValidator.isValidGiverName("Rama Rao"))
        assertTrue(GiftValidator.isValidGiverName("A"))
    }

    @Test
    fun `zero amount is invalid`() {
        assertFalse(GiftValidator.isValidAmount(0.0))
        assertFalse(GiftValidator.isValidAmount(-100.0))
    }

    @Test
    fun `positive amount is valid`() {
        assertTrue(GiftValidator.isValidAmount(1.0))
        assertTrue(GiftValidator.isValidAmount(500.0))
        assertTrue(GiftValidator.isValidAmount(0.01))
    }

    @Test
    fun `parseAmount returns null for invalid strings`() {
        assertNull(GiftValidator.parseAmount(""))
        assertNull(GiftValidator.parseAmount("abc"))
        assertNull(GiftValidator.parseAmount("0"))
        assertNull(GiftValidator.parseAmount("-50"))
    }

    @Test
    fun `parseAmount returns value for valid amount string`() {
        assertNotNull(GiftValidator.parseAmount("500"))
        assertNotNull(GiftValidator.parseAmount("0.01"))
        assertNotNull(GiftValidator.parseAmount("1000.50"))
    }
}
