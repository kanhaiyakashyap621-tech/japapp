package com.shantjap.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shantjap.app.data.local.entities.JourneyEntity

@Dao
interface JourneyDao {
    @Query("SELECT * FROM journey WHERE id = 1")
    suspend fun getJourney(): JourneyEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: JourneyEntity)
}
