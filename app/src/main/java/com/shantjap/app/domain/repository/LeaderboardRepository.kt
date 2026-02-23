package com.shantjap.app.domain.repository

import com.shantjap.app.domain.model.LeaderboardEntry
import com.shantjap.app.domain.model.Mode

interface LeaderboardRepository {
    suspend fun syncCounts(
        mode: Mode,
        todayCount: Int,
        weeklyCount: Int,
        lifetimeCount: Long
    )
    suspend fun getDailyRanking(): List<LeaderboardEntry>
    suspend fun getWeeklyRanking(): List<LeaderboardEntry>
    suspend fun getLifetimeRanking(): List<LeaderboardEntry>
    suspend fun getGlobalTotal(): Long
}
