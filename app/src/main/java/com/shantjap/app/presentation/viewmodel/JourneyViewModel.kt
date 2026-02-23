package com.shantjap.app.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.shantjap.app.ShantJapApp
import com.shantjap.app.domain.model.JourneyState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class JourneyViewModel(app: Application) : AndroidViewModel(app) {
    private val container = (app as ShantJapApp).container
    private val counterRepository = container.counterRepository

    private val _state = MutableStateFlow(
        JourneyState(active = false, currentDay = 0, completed = false, lastUnlockDay = 0, lastActiveDate = "")
    )
    val state: StateFlow<JourneyState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _state.value = counterRepository.getJourneyState()
        }
    }

    fun joinJourney() {
        viewModelScope.launch {
            _state.value = counterRepository.joinJourney()
        }
    }
}
