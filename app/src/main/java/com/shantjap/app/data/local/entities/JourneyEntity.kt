package com.shantjap.app.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "journey")
data class JourneyEntity(
    @PrimaryKey val id: Int = 1,
    val active: Boolean,
    val startDate: String,
    val lastActiveDate: String,
    val currentDay: Int,
    val completed: Boolean,
    val lastUnlockDay: Int
)
