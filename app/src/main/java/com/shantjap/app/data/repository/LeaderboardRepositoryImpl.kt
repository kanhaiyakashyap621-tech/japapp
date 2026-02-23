package com.shantjap.app.data.repository

import android.content.Context
import com.shantjap.app.data.remote.FirestoreService
import com.shantjap.app.domain.model.LeaderboardEntry
import com.shantjap.app.domain.model.Mode
import com.shantjap.app.domain.repository.LeaderboardRepository
import com.shantjap.app.domain.repository.UserProfileRepository
import com.shantjap.app.utils.DateUtils
import com.shantjap.app.utils.DeviceIdUtils

class LeaderboardRepositoryImpl(
    private val firestoreService: FirestoreService,
    private val userProfileRepository: UserProfileRepository,
    private val appContext: Context
) : LeaderboardRepository {

    override suspend fun syncCounts(
        mode: Mode,
        todayCount: Int,
        weeklyCount: Int,
        lifetimeCount: Long
    ) {
        val profile = userProfileRepository.getProfile() ?: return
        val deviceId = DeviceIdUtils.getDeviceId(appContext)
        val now = System.currentTimeMillis()
        val dailyEntry = LeaderboardEntry(
            deviceId = deviceId,
            name = profile.name,
            city = profile.city,
            country = profile.country,
            mode = mode,
            count = todayCount.toLong(),
            updatedAt = now
        )
        val weeklyEntry = dailyEntry.copy(count = weeklyCount.toLong())
        val lifetimeEntry = dailyEntry.copy(count = lifetimeCount)

        firestoreService.upsertDailyEntry(DateUtils.todayKey(), dailyEntry)
        firestoreService.upsertWeeklyEntry(DateUtils.weekKey(), weeklyEntry)
        firestoreService.upsertLifetimeEntry(lifetimeEntry)

        val delta = lifetimeCount - profile.lastSyncedLifetime
        if (delta > 0) {
            firestoreService.incrementGlobalTotal(delta)
            userProfileRepository.saveProfile(profile.copy(lastSyncedLifetime = lifetimeCount))
        }
    }

    override suspend fun getDailyRanking(): List<LeaderboardEntry> {
        return firestoreService.getDailyRanking(DateUtils.todayKey(), 50)
    }

    override suspend fun getWeeklyRanking(): List<LeaderboardEntry> {
        return firestoreService.getWeeklyRanking(DateUtils.weekKey(), 50)
    }

    override suspend fun getLifetimeRanking(): List<LeaderboardEntry> {
        return firestoreService.getLifetimeRanking(50)
    }

    override suspend fun getGlobalTotal(): Long {
        return firestoreService.getGlobalTotal()
    }
}
