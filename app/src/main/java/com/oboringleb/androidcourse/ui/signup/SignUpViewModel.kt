package com.oboringleb.androidcourse.ui.signup

import androidx.lifecycle.viewModelScope
import com.oboringleb.androidcourse.repository.AuthRepository
import com.oboringleb.androidcourse.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class SignUpViewModel: BaseViewModel() {
    fun signUp() {
        viewModelScope.launch {
            AuthRepository.signIn()
        }
    }
}