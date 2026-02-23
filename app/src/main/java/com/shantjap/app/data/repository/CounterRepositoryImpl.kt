package com.shantjap.app.data.repository

import com.shantjap.app.data.local.dao.CountDao
import com.shantjap.app.data.local.dao.JourneyDao
import com.shantjap.app.data.local.entities.DailyCountEntity
import com.shantjap.app.data.local.entities.JourneyEntity
import com.shantjap.app.data.local.entities.LifetimeCountEntity
import com.shantjap.app.domain.model.Counts
import com.shantjap.app.domain.model.JourneyState
import com.shantjap.app.domain.model.Mode
import com.shantjap.app.domain.repository.CounterRepository
import com.shantjap.app.utils.DateUtils

class CounterRepositoryImpl(
    private val countDao: CountDao,
    private val journeyDao: JourneyDao
) : CounterRepository {

    override suspend fun increment(mode: Mode, delta: Int) {
        val today = DateUtils.todayKey()
        val modeKey = mode.name

        val currentDaily = countDao.getDailyCount(today, modeKey) ?: 0
        countDao.upsertDailyCount(
            DailyCountEntity(date = today, mode = modeKey, count = currentDaily + delta)
        )

        val currentLifetime = countDao.getLifetimeCount(modeKey) ?: 0L
        countDao.upsertLifetimeCount(
            LifetimeCountEntity(mode = modeKey, count = currentLifetime + delta)
        )

        updateJourneyIfNeeded()
    }

    override suspend fun getCounts(mode: Mode): Counts {
        val today = DateUtils.todayKey()
        val modeKey = mode.name
        val todayCount = countDao.getDailyCount(today, modeKey) ?: 0
        val lifetime = countDao.getLifetimeCount(modeKey) ?: 0L
        val streak = computeStreak(mode)
        return Counts(today = todayCount, lifetime = lifetime, streak = streak)
    }

    override suspend fun getWeeklyCount(mode: Mode): Int {
        val modeKey = mode.name
        val recent = countDao.getRecentDailyCounts(modeKey, 7)
        val map = recent.associateBy { it.date }
        var total = 0
        var date = DateUtils.today()
        repeat(7) {
            val key = DateUtils.format(date)
            total += map[key]?.count ?: 0
            date = date.minusDays(1)
        }
        return total
    }

    override suspend fun getJourneyState(): JourneyState {
        val entity = journeyDao.getJourney()
        return if (entity == null) {
            JourneyState(
                active = false,
                currentDay = 0,
                completed = false,
                lastUnlockDay = 0,
                lastActiveDate = ""
            )
        } else {
            JourneyState(
                active = entity.active,
                currentDay = entity.currentDay,
                completed = entity.completed,
                lastUnlockDay = entity.lastUnlockDay,
                lastActiveDate = entity.lastActiveDate
            )
        }
    }

    override suspend fun joinJourney(): JourneyState {
        val today = DateUtils.todayKey()
        val entity = JourneyEntity(
            active = true,
            startDate = today,
            lastActiveDate = today,
            currentDay = 1,
            completed = false,
            lastUnlockDay = 0
        )
        journeyDao.upsert(entity)
        return JourneyState(
            active = true,
            currentDay = 1,
            completed = false,
            lastUnlockDay = 0,
            lastActiveDate = today
        )
    }

    private suspend fun updateJourneyIfNeeded() {
        val entity = journeyDao.getJourney() ?: return
        if (!entity.active || entity.completed) return

        val today = DateUtils.todayKey()
        if (entity.lastActiveDate == today) return

        val gapDays = DateUtils.daysBetween(entity.lastActiveDate, today)
        val newDay = if (gapDays == 1) entity.currentDay + 1 else 1
        val completed = newDay >= 108
        val newUnlockDay = if (newDay % 12 == 0) newDay else entity.lastUnlockDay

        journeyDao.upsert(
            entity.copy(
                lastActiveDate = today,
                currentDay = newDay,
                completed = completed,
                lastUnlockDay = newUnlockDay
            )
        )
    }

    private suspend fun computeStreak(mode: Mode): Int {
        val modeKey = mode.name
        val recent = countDao.getRecentDailyCounts(modeKey, 120)
        if (recent.isEmpty()) return 0

        val map = recent.associateBy { it.date }
        var date = DateUtils.today()
        var streak = 0
        while (true) {
            val key = DateUtils.format(date)
            val count = map[key]?.count ?: 0
            if (count > 0) {
                streak += 1
                date = date.minusDays(1)
            } else {
                break
            }
        }
        return streak
    }
}
