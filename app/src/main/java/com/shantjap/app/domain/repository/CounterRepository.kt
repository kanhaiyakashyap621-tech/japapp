package com.shantjap.app.domain.repository

import com.shantjap.app.domain.model.Counts
import com.shantjap.app.domain.model.JourneyState
import com.shantjap.app.domain.model.Mode

interface CounterRepository {
    suspend fun increment(mode: Mode, delta: Int = 1)
    suspend fun getCounts(mode: Mode): Counts
    suspend fun getWeeklyCount(mode: Mode): Int
    suspend fun getJourneyState(): JourneyState
    suspend fun joinJourney(): JourneyState
}
