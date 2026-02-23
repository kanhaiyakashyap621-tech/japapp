package com.shantjap.app.data.local.entities

import androidx.room.Entity

@Entity(tableName = "daily_counts", primaryKeys = ["date", "mode"])
data class DailyCountEntity(
    val date: String,
    val mode: String,
    val count: Int
)
