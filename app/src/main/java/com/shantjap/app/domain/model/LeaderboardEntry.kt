package com.shantjap.app.domain.model

data class LeaderboardEntry(
    val deviceId: String,
    val name: String,
    val city: String,
    val country: String,
    val mode: Mode,
    val count: Long,
    val updatedAt: Long
)
