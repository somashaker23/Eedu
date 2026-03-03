package com.eedu.gifttracker.util

object GiftValidator {

    fun isValidGiverName(name: String): Boolean = name.trim().isNotEmpty()

    fun isValidAmount(amount: Double): Boolean = amount > 0

    fun parseAmount(amountStr: String): Double? {
        val amount = amountStr.trim().toDoubleOrNull() ?: return null
        return if (isValidAmount(amount)) amount else null
    }
}
