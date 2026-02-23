package com.shantjap.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shantjap.app.data.local.entities.DailyCountEntity
import com.shantjap.app.data.local.entities.LifetimeCountEntity

@Dao
interface CountDao {
    @Query("SELECT count FROM daily_counts WHERE date = :date AND mode = :mode")
    suspend fun getDailyCount(date: String, mode: String): Int?

    @Query("SELECT count FROM lifetime_counts WHERE mode = :mode")
    suspend fun getLifetimeCount(mode: String): Long?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertDailyCount(entity: DailyCountEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertLifetimeCount(entity: LifetimeCountEntity)

    @Query("SELECT date, count, mode FROM daily_counts WHERE mode = :mode ORDER BY date DESC LIMIT :limit")
    suspend fun getRecentDailyCounts(mode: String, limit: Int): List<DailyCountEntity>
}
