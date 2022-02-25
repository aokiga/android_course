package com.oboringleb.androidcourse.ui.onboarding

import androidx.lifecycle.viewModelScope
import com.oboringleb.androidcourse.ui.base.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class OnboardingViewModel: BaseViewModel() {
    private val _videoSoundState = MutableStateFlow<Boolean>(false)

    val videoSoundState: Flow<Boolean>
        get() = _videoSoundState.asStateFlow()

    fun changeVideoSoundState() {
        viewModelScope.launch {
            val newVideoSoundState: Boolean = !_videoSoundState.value
            _videoSoundState.emit(newVideoSoundState)
        }
    }

    private val previousScrollEventTimeFlow = MutableStateFlow(System.currentTimeMillis())

    private val _scrollIndexFlow = MutableStateFlow(0)
    val scrollIndexFlow: StateFlow<Int>
        get() = _scrollIndexFlow.asStateFlow()

    fun registerScrollEvent(position: Int) {
        viewModelScope.launch {
            previousScrollEventTimeFlow.emit(System.currentTimeMillis())
            _scrollIndexFlow.emit(position)
        }
    }

    private val TIMEOUT_MS: Long = 4000L

    suspend fun launchAutoScroll(initialIndex: Int, itemsCount: Int) {
        with(_scrollIndexFlow) {
            emit(initialIndex)
        }
        while (true) {
            val currentTimeMs = System.currentTimeMillis()
            val previousTime = previousScrollEventTimeFlow.value
            if (currentTimeMs - previousTime >= TIMEOUT_MS) {
                previousScrollEventTimeFlow.emit(System.currentTimeMillis())
                with(_scrollIndexFlow) {
                    emit((value + 1) % itemsCount)
                }
            }
            delay(50L)
        }
    }
}