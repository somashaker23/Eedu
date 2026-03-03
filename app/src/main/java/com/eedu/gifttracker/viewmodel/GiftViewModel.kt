package com.eedu.gifttracker.viewmodel

import androidx.lifecycle.*
import com.eedu.gifttracker.data.database.entities.Gift
import com.eedu.gifttracker.data.repository.GiftRepository
import kotlinx.coroutines.launch

class GiftViewModel(private val repository: GiftRepository) : ViewModel() {

    private var _eventId: Long = 0

    private val _gifts = MutableLiveData<List<Gift>>()
    val gifts: LiveData<List<Gift>> = _gifts

    private val _totalAmount = MutableLiveData<Double>()
    val totalAmount: LiveData<Double> = _totalAmount

    private val _giftCount = MutableLiveData<Int>()
    val giftCount: LiveData<Int> = _giftCount

    private val _insertResult = MutableLiveData<Long>()
    val insertResult: LiveData<Long> = _insertResult

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private var giftsLiveData: LiveData<List<Gift>>? = null

    fun getGiftsForEvent(eventId: Long): LiveData<List<Gift>> {
        _eventId = eventId
        return repository.getGiftsForEvent(eventId)
    }

    fun loadEventSummary(eventId: Long) {
        viewModelScope.launch {
            try {
                val count = repository.getGiftCountForEvent(eventId)
                val total = repository.getTotalAmountForEvent(eventId)
                _giftCount.postValue(count)
                _totalAmount.postValue(total)
            } catch (e: Exception) {
                _error.postValue(e.message ?: "Error loading summary")
            }
        }
    }

    fun insertGift(gift: Gift) {
        viewModelScope.launch {
            try {
                val id = repository.insertGift(gift)
                _insertResult.postValue(id)
            } catch (e: Exception) {
                _error.postValue(e.message ?: "Error inserting gift")
            }
        }
    }

    fun updateGift(gift: Gift) {
        viewModelScope.launch {
            try {
                repository.updateGift(gift)
            } catch (e: Exception) {
                _error.postValue(e.message ?: "Error updating gift")
            }
        }
    }

    fun deleteGift(gift: Gift) {
        viewModelScope.launch {
            try {
                repository.deleteGift(gift)
            } catch (e: Exception) {
                _error.postValue(e.message ?: "Error deleting gift")
            }
        }
    }

    class Factory(private val repository: GiftRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(GiftViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return GiftViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
