package com.eedu.gifttracker.data.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "gifts",
    foreignKeys = [ForeignKey(
        entity = Event::class,
        parentColumns = ["id"],
        childColumns = ["eventId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["eventId"])]
)
data class Gift(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val eventId: Long,
    val giverName: String,
    val amount: Double,
    val relationship: String,
    val paymentMethod: String,
    val village: String = "",
    val notes: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
