package com.shantjap.app.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.shantjap.app.ShantJapApp
import com.shantjap.app.domain.model.Counts
import com.shantjap.app.domain.model.Mode
import com.shantjap.app.domain.model.Quote
import com.shantjap.app.domain.model.Sankalp
import com.shantjap.app.utils.DateUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(app: Application) : AndroidViewModel(app) {
    private val container = (app as ShantJapApp).container
    private val counterRepository = container.counterRepository
    private val quoteRepository = container.quoteRepository
    private val sankalpRepository = container.sankalpRepository

    private val _mode = MutableStateFlow(Mode.RADHA)
    val mode: StateFlow<Mode> = _mode.asStateFlow()

    private val _counts = MutableStateFlow(Counts(0, 0, 0))
    val counts: StateFlow<Counts> = _counts.asStateFlow()

    private val _quote = MutableStateFlow<Quote?>(null)
    val quote: StateFlow<Quote?> = _quote.asStateFlow()

    private val _sankalpReminder = MutableStateFlow<Sankalp?>(null)
    val sankalpReminder: StateFlow<Sankalp?> = _sankalpReminder.asStateFlow()

    init {
        refreshAll()
    }

    fun setMode(newMode: Mode) {
        _mode.value = newMode
        refreshCounts()
    }

    fun onTap() {
        viewModelScope.launch {
            counterRepository.increment(_mode.value, 1)
            refreshCounts()
            refreshSankalpReminder()
        }
    }

    fun refreshAll() {
        refreshCounts()
        refreshQuote()
        refreshSankalpReminder()
    }

    fun dismissSankalpReminder() {
        viewModelScope.launch {
            sankalpRepository.markReminderShown(DateUtils.todayKey())
            _sankalpReminder.value = null
        }
    }

    private fun refreshCounts() {
        viewModelScope.launch {
            _counts.value = counterRepository.getCounts(_mode.value)
        }
    }

    private fun refreshQuote() {
        viewModelScope.launch {
            val day = DateUtils.dayOfYear()
            _quote.value = quoteRepository.getQuoteForDay(day)
        }
    }

    private fun refreshSankalpReminder() {
        viewModelScope.launch {
            val sankalp = sankalpRepository.getSankalp()
            val today = DateUtils.todayKey()
            _sankalpReminder.value = if (sankalp != null && sankalp.text.isNotBlank() && sankalp.lastReminderDate != today) {
                sankalp
            } else {
                null
            }
        }
    }
}
