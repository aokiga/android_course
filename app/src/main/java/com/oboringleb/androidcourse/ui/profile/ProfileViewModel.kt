package com.oboringleb.androidcourse.ui.profile

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.View
import androidx.lifecycle.viewModelScope
import com.oboringleb.androidcourse.entity.User
import com.oboringleb.androidcourse.repository.AuthRepository
import com.oboringleb.androidcourse.ui.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

fun loadProfile() = Gleb

class ProfileViewModel: BaseViewModel() {
    val heart = SmileyCounter("❤️️")
    val poo = SmileyCounter("\uD83D\uDCA9")
    val laugh = SmileyCounter("\uD83D\uDE02")

    private val _viewState = MutableStateFlow<ViewState>(ViewState.Loading)
    val viewState: Flow<ViewState> get() = _viewState.asStateFlow()

    val image: MutableStateFlow<Bitmap?> = MutableStateFlow(null)

    fun inc(smileyCounter: SmileyCounter) = viewModelScope.launch {  smileyCounter.inc() }

    suspend fun loadProfile() {
        withContext(Dispatchers.IO) {
            _viewState.emit(ViewState.Loading)
            val picture = loadProfilePicture(Gleb.avatarURL)
            image.emit(picture)
            _viewState.emit(ViewState.Data(Gleb))
        }
    }

    private fun loadProfilePicture(url: String): Bitmap {
        return BitmapFactory.decodeStream(java.net.URL(url).openStream())
    }

    sealed class ViewState {
        object Loading : ViewState()
        data class Data(val profile: User) : ViewState()
    }

    fun logout() {
        viewModelScope.launch {
            AuthRepository.logout()
        }
    }
}