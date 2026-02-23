package com.shantjap.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shantjap.app.data.local.entities.SankalpEntity

@Dao
interface SankalpDao {
    @Query("SELECT * FROM sankalp WHERE id = 1")
    suspend fun getSankalp(): SankalpEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: SankalpEntity)
}
