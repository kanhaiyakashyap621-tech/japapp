package com.shantjap.app.domain.model

data class JourneyState(
    val active: Boolean,
    val currentDay: Int,
    val completed: Boolean,
    val lastUnlockDay: Int,
    val lastActiveDate: String
)
