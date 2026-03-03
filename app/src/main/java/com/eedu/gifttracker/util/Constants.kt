package com.eedu.gifttracker.util

object Constants {
    const val FREE_TIER_GIFT_LIMIT = 100
    const val PAYMENT_AMOUNT_PAISE = 10000 // ₹100 in paise
    
    val EVENT_TYPES = listOf(
        "Wedding",
        "Seemantham",
        "Upanayanam",
        "Gruhapravesam",
        "Birthday",
        "Engagement",
        "Namakaranam",
        "Other"
    )
    
    val RELATIONSHIPS = listOf(
        "Mama",
        "Mavayya",
        "Attha",
        "Nana",
        "Tatha",
        "Akka",
        "Anna",
        "Friend",
        "Colleague",
        "Neighbor",
        "Relative",
        "Other"
    )
    
    val PAYMENT_METHODS = listOf(
        "Cash",
        "UPI",
        "Other"
    )
    
    const val EXTRA_EVENT_ID = "extra_event_id"
    const val EXTRA_GIFT_ID = "extra_gift_id"
    const val PREFS_NAME = "eedu_prefs"
    const val PREF_PAYMENT_DONE = "payment_done"
}
