package com.shantjap.app.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.shantjap.app.ShantJapApp
import com.shantjap.app.domain.model.LeaderboardEntry
import com.shantjap.app.domain.model.Mode
import com.shantjap.app.domain.model.UserProfile
import com.shantjap.app.utils.CountryUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LeaderboardViewModel(app: Application) : AndroidViewModel(app) {
    private val container = (app as ShantJapApp).container
    private val counterRepository = container.counterRepository
    private val leaderboardRepository = container.leaderboardRepository
    private val userProfileRepository = container.userProfileRepository

    private val _mode = MutableStateFlow(Mode.RADHA)
    val mode: StateFlow<Mode> = _mode.asStateFlow()

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name.asStateFlow()

    private val _city = MutableStateFlow("")
    val city: StateFlow<String> = _city.asStateFlow()

    private val _country = MutableStateFlow(CountryUtils.detectCountry())
    val country: StateFlow<String> = _country.asStateFlow()

    private val _daily = MutableStateFlow<List<LeaderboardEntry>>(emptyList())
    val daily: StateFlow<List<LeaderboardEntry>> = _daily.asStateFlow()

    private val _weekly = MutableStateFlow<List<LeaderboardEntry>>(emptyList())
    val weekly: StateFlow<List<LeaderboardEntry>> = _weekly.asStateFlow()

    private val _lifetime = MutableStateFlow<List<LeaderboardEntry>>(emptyList())
    val lifetime: StateFlow<List<LeaderboardEntry>> = _lifetime.asStateFlow()

    private val _globalTotal = MutableStateFlow(0L)
    val globalTotal: StateFlow<Long> = _globalTotal.asStateFlow()

    private val _syncing = MutableStateFlow(false)
    val syncing: StateFlow<Boolean> = _syncing.asStateFlow()

    init {
        loadProfile()
        refreshRankings()
        refreshGlobalTotal()
    }

    fun setMode(newMode: Mode) {
        _mode.value = newMode
    }

    fun updateName(value: String) {
        _name.value = value
    }

    fun updateCity(value: String) {
        _city.value = value
    }

    fun saveProfile() {
        viewModelScope.launch {
            val existing = userProfileRepository.getProfile()
            val profile = UserProfile(
                name = _name.value.trim(),
                city = _city.value.trim(),
                country = _country.value,
                lastSyncedLifetime = existing?.lastSyncedLifetime ?: 0
            )
            if (profile.name.isNotBlank() && profile.city.isNotBlank()) {
                userProfileRepository.saveProfile(profile)
            }
        }
    }

    fun syncCounts() {
        viewModelScope.launch {
            _syncing.value = true
            runCatching {
                val counts = counterRepository.getCounts(_mode.value)
                val weeklyCount = counterRepository.getWeeklyCount(_mode.value)
                leaderboardRepository.syncCounts(
                    mode = _mode.value,
                    todayCount = counts.today,
                    weeklyCount = weeklyCount,
                    lifetimeCount = counts.lifetime
                )
                refreshRankings()
                refreshGlobalTotal()
            }
            _syncing.value = false
        }
    }

    fun refreshRankings() {
        viewModelScope.launch {
            runCatching {
                _daily.value = leaderboardRepository.getDailyRanking()
                _weekly.value = leaderboardRepository.getWeeklyRanking()
                _lifetime.value = leaderboardRepository.getLifetimeRanking()
            }
        }
    }

    private fun refreshGlobalTotal() {
        viewModelScope.launch {
            runCatching {
                _globalTotal.value = leaderboardRepository.getGlobalTotal()
            }
        }
    }

    private fun loadProfile() {
        viewModelScope.launch {
            val profile = userProfileRepository.getProfile()
            if (profile != null) {
                _name.value = profile.name
                _city.value = profile.city
                _country.value = profile.country
            }
        }
    }
}
