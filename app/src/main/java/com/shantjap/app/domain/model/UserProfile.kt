package com.shantjap.app.domain.model

data class UserProfile(
    val name: String,
    val city: String,
    val country: String,
    val lastSyncedLifetime: Long = 0
)
