package com.eedu.gifttracker.viewmodel

import android.content.Context
import androidx.lifecycle.*
import com.eedu.gifttracker.data.database.entities.Event
import com.eedu.gifttracker.data.database.entities.Gift
import com.eedu.gifttracker.data.repository.EventRepository
import com.eedu.gifttracker.data.repository.GiftRepository
import com.eedu.gifttracker.util.ExcelExporter
import com.eedu.gifttracker.util.PdfExporter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class ExportViewModel(
    private val eventRepository: EventRepository,
    private val giftRepository: GiftRepository
) : ViewModel() {

    private val _exportedFile = MutableLiveData<File?>()
    val exportedFile: LiveData<File?> = _exportedFile

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun exportToPdf(context: Context, event: Event, gifts: List<Gift>) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                val file = withContext(Dispatchers.IO) {
                    PdfExporter(context).exportGiftsToPdf(event, gifts)
                }
                _exportedFile.postValue(file)
            } catch (e: Exception) {
                _error.postValue("PDF export failed: ${e.message}")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun exportToExcel(context: Context, event: Event, gifts: List<Gift>) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                val file = withContext(Dispatchers.IO) {
                    ExcelExporter(context).exportGiftsToExcel(event, gifts)
                }
                _exportedFile.postValue(file)
            } catch (e: Exception) {
                _error.postValue("Excel export failed: ${e.message}")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    class Factory(
        private val eventRepository: EventRepository,
        private val giftRepository: GiftRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ExportViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ExportViewModel(eventRepository, giftRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
