package com.eedu.gifttracker.repository

import com.eedu.gifttracker.data.database.dao.GiftDao
import com.eedu.gifttracker.data.database.entities.Gift
import com.eedu.gifttracker.data.repository.GiftRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class GiftRepositoryTest {

    @Mock
    private lateinit var giftDao: GiftDao

    private lateinit var repository: GiftRepository

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        repository = GiftRepository(giftDao)
    }

    @Test
    fun `insertGift delegates to dao`() = runTest {
        val gift = Gift(
            eventId = 1L,
            giverName = "Test User",
            amount = 500.0,
            relationship = "Friend",
            paymentMethod = "Cash"
        )
        whenever(giftDao.insertGift(gift)).thenReturn(1L)

        val result = repository.insertGift(gift)

        assertEquals(1L, result)
        verify(giftDao).insertGift(gift)
    }

    @Test
    fun `getTotalAmountForEvent returns correct total`() = runTest {
        whenever(giftDao.getTotalAmountForEvent(1L)).thenReturn(1500.0)

        val result = repository.getTotalAmountForEvent(1L)

        assertEquals(1500.0, result, 0.001)
        verify(giftDao).getTotalAmountForEvent(1L)
    }

    @Test
    fun `deleteGift delegates to dao`() = runTest {
        val gift = Gift(
            id = 1L,
            eventId = 1L,
            giverName = "Test",
            amount = 100.0,
            relationship = "Friend",
            paymentMethod = "Cash"
        )

        repository.deleteGift(gift)

        verify(giftDao).deleteGift(gift)
    }

    @Test
    fun `getGiftCountForEvent returns correct count`() = runTest {
        whenever(giftDao.getGiftCountForEvent(1L)).thenReturn(5)

        val result = repository.getGiftCountForEvent(1L)

        assertEquals(5, result)
    }
}
