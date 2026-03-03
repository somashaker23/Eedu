package com.eedu.gifttracker.viewmodel

import androidx.lifecycle.*
import com.eedu.gifttracker.data.database.entities.Event
import com.eedu.gifttracker.data.repository.EventRepository
import kotlinx.coroutines.launch

class EventViewModel(private val repository: EventRepository) : ViewModel() {

    val allEvents: LiveData<List<Event>> = repository.allEvents

    private val _insertResult = MutableLiveData<Long>()
    val insertResult: LiveData<Long> = _insertResult

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun insertEvent(event: Event) {
        viewModelScope.launch {
            try {
                val id = repository.insertEvent(event)
                _insertResult.postValue(id)
            } catch (e: Exception) {
                _error.postValue(e.message ?: "Error inserting event")
            }
        }
    }

    fun deleteEvent(event: Event) {
        viewModelScope.launch {
            try {
                repository.deleteEvent(event)
            } catch (e: Exception) {
                _error.postValue(e.message ?: "Error deleting event")
            }
        }
    }

    fun updateEvent(event: Event) {
        viewModelScope.launch {
            try {
                repository.updateEvent(event)
            } catch (e: Exception) {
                _error.postValue(e.message ?: "Error updating event")
            }
        }
    }

    class Factory(private val repository: EventRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(EventViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return EventViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
