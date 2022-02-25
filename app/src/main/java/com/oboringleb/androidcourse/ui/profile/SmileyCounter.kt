package com.oboringleb.androidcourse.ui.profile

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SmileyCounter(val code: String) {
    private val _smileyStateFlow = MutableStateFlow(0)

    fun get() = _smileyStateFlow.asStateFlow()

    suspend fun inc() {
        val x = _smileyStateFlow.value
        _smileyStateFlow.emit(x + 1)
    }
}