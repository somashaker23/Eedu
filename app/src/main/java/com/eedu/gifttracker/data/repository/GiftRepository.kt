package com.eedu.gifttracker.data.repository

import androidx.lifecycle.LiveData
import com.eedu.gifttracker.data.database.dao.GiftDao
import com.eedu.gifttracker.data.database.entities.Gift

class GiftRepository(private val giftDao: GiftDao) {

    fun getGiftsForEvent(eventId: Long): LiveData<List<Gift>> {
        return giftDao.getGiftsForEvent(eventId)
    }

    suspend fun getGiftsForEventList(eventId: Long): List<Gift> {
        return giftDao.getGiftsForEventList(eventId)
    }

    suspend fun insertGift(gift: Gift): Long {
        return giftDao.insertGift(gift)
    }

    suspend fun updateGift(gift: Gift) {
        giftDao.updateGift(gift)
    }

    suspend fun deleteGift(gift: Gift) {
        giftDao.deleteGift(gift)
    }

    suspend fun getGiftById(giftId: Long): Gift? {
        return giftDao.getGiftById(giftId)
    }

    suspend fun getGiftCountForEvent(eventId: Long): Int {
        return giftDao.getGiftCountForEvent(eventId)
    }

    suspend fun getTotalAmountForEvent(eventId: Long): Double {
        return giftDao.getTotalAmountForEvent(eventId)
    }

    suspend fun getTotalGiftCount(): Int {
        return giftDao.getTotalGiftCount()
    }
}
