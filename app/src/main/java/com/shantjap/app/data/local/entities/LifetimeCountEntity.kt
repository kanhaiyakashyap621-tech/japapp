package com.shantjap.app.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lifetime_counts")
data class LifetimeCountEntity(
    @PrimaryKey val mode: String,
    val count: Long
)
