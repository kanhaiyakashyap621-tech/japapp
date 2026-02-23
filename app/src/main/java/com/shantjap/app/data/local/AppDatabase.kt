package com.shantjap.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.shantjap.app.data.local.dao.CountDao
import com.shantjap.app.data.local.dao.JourneyDao
import com.shantjap.app.data.local.dao.SankalpDao
import com.shantjap.app.data.local.dao.UserProfileDao
import com.shantjap.app.data.local.entities.DailyCountEntity
import com.shantjap.app.data.local.entities.JourneyEntity
import com.shantjap.app.data.local.entities.LifetimeCountEntity
import com.shantjap.app.data.local.entities.SankalpEntity
import com.shantjap.app.data.local.entities.UserProfileEntity

@Database(
    entities = [
        DailyCountEntity::class,
        LifetimeCountEntity::class,
        SankalpEntity::class,
        JourneyEntity::class,
        UserProfileEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun countDao(): CountDao
    abstract fun sankalpDao(): SankalpDao
    abstract fun journeyDao(): JourneyDao
    abstract fun userProfileDao(): UserProfileDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "shant_jap.db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
