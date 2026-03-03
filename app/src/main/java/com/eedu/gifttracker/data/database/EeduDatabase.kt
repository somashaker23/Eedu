package com.eedu.gifttracker.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.eedu.gifttracker.data.database.dao.EventDao
import com.eedu.gifttracker.data.database.dao.GiftDao
import com.eedu.gifttracker.data.database.entities.Event
import com.eedu.gifttracker.data.database.entities.Gift

@Database(
    entities = [Event::class, Gift::class],
    version = 1,
    exportSchema = false
)
abstract class EeduDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao
    abstract fun giftDao(): GiftDao

    companion object {
        @Volatile
        private var INSTANCE: EeduDatabase? = null

        fun getDatabase(context: Context): EeduDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    EeduDatabase::class.java,
                    "eedu_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
