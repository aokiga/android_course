package com.oboringleb.androidcourse.ui.signin

import androidx.lifecycle.viewModelScope
import com.oboringleb.androidcourse.repository.AuthRepository
import com.oboringleb.androidcourse.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class SignInViewModel: BaseViewModel() {
    fun signIn() {
        viewModelScope.launch {
            AuthRepository.signIn()
        }
    }
}