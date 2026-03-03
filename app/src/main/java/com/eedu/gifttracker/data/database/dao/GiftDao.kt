package com.eedu.gifttracker.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.eedu.gifttracker.data.database.entities.Gift

@Dao
interface GiftDao {
    @Query("SELECT * FROM gifts WHERE eventId = :eventId ORDER BY timestamp DESC")
    fun getGiftsForEvent(eventId: Long): LiveData<List<Gift>>

    @Query("SELECT * FROM gifts WHERE eventId = :eventId ORDER BY timestamp DESC")
    suspend fun getGiftsForEventList(eventId: Long): List<Gift>

    @Query("SELECT COUNT(*) FROM gifts WHERE eventId = :eventId")
    suspend fun getGiftCountForEvent(eventId: Long): Int

    @Query("SELECT COALESCE(SUM(amount), 0) FROM gifts WHERE eventId = :eventId")
    suspend fun getTotalAmountForEvent(eventId: Long): Double

    @Query("SELECT COUNT(*) FROM gifts")
    suspend fun getTotalGiftCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGift(gift: Gift): Long

    @Update
    suspend fun updateGift(gift: Gift)

    @Delete
    suspend fun deleteGift(gift: Gift)

    @Query("SELECT * FROM gifts WHERE id = :giftId")
    suspend fun getGiftById(giftId: Long): Gift?
}
