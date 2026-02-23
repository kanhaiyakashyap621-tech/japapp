package com.shantjap.app.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.shantjap.app.ShantJapApp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SankalpViewModel(app: Application) : AndroidViewModel(app) {
    private val container = (app as ShantJapApp).container
    private val sankalpRepository = container.sankalpRepository

    private val _text = MutableStateFlow("")
    val text: StateFlow<String> = _text.asStateFlow()

    init {
        load()
    }

    fun updateText(value: String) {
        _text.value = value
    }

    fun save() {
        viewModelScope.launch {
            sankalpRepository.saveSankalp(_text.value.trim())
        }
    }

    private fun load() {
        viewModelScope.launch {
            val sankalp = sankalpRepository.getSankalp()
            if (sankalp != null) {
                _text.value = sankalp.text
            }
        }
    }
}
