package com.eedu.gifttracker.data.repository

import androidx.lifecycle.LiveData
import com.eedu.gifttracker.data.database.dao.EventDao
import com.eedu.gifttracker.data.database.entities.Event

class EventRepository(private val eventDao: EventDao) {

    val allEvents: LiveData<List<Event>> = eventDao.getAllEvents()

    suspend fun insertEvent(event: Event): Long {
        return eventDao.insertEvent(event)
    }

    suspend fun updateEvent(event: Event) {
        eventDao.updateEvent(event)
    }

    suspend fun deleteEvent(event: Event) {
        eventDao.deleteEvent(event)
    }

    suspend fun getEventById(eventId: Long): Event? {
        return eventDao.getEventById(eventId)
    }

    suspend fun getAllEventsList(): List<Event> {
        return eventDao.getAllEventsList()
    }
}
