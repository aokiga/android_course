package com.oboringleb.androidcourse.ui.emailconfirmation

import androidx.lifecycle.viewModelScope
import com.oboringleb.androidcourse.repository.AuthRepository
import com.oboringleb.androidcourse.ui.base.BaseViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class EmailConfirmationViewModel: BaseViewModel() {
    private val _timeLeftFlow = MutableStateFlow(0L)
    private val _currentCodeFlow = MutableStateFlow<Long?>(null)

    val timeLeftFlow: StateFlow<Long>
        get() = _timeLeftFlow.asStateFlow()

    val currentCodeFlow: StateFlow<Long?>
        get() = _currentCodeFlow.asStateFlow()

    val ATTEMPT_TIME_SEC = 30L

    fun confirmCode(
        firstname: String,
        lastname: String,
        nickname: String,
        email: String,
        password: String,
        code: String
    ) {
        viewModelScope.launch {
            try {
                if (_timeLeftFlow.value > 0) {
                    AuthRepository.verifyCode(
                        firstname,
                        lastname,
                        nickname,
                        email,
                        password,
                        code
                    )
                }
            } catch (error: Exception) {
            }
        }
    }

    private fun generateNewCode() = 42L

    suspend fun askNewCode() {
        withTimeout(ATTEMPT_TIME_SEC * 1000L + 1000) {
            _timeLeftFlow.emit(ATTEMPT_TIME_SEC)
            _currentCodeFlow.emit(generateNewCode())
            while (_timeLeftFlow.value > 0) {
                _timeLeftFlow.value--
                delay(1000L)
            }
            _currentCodeFlow.emit(null)
        }
    }
}