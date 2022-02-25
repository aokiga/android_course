package com.oboringleb.androidcourse.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object AuthRepository {
    private val _isAuthorizedFlow = MutableStateFlow(false)
    val isAuthorizedFlow = _isAuthorizedFlow.asStateFlow()

    suspend fun signIn(
        email: String,
        password: String
    ) {
        _isAuthorizedFlow.emit(true)
        //TODO: Get API response for email availability, change screen to email confirm
    }

    suspend fun signUp(
        firstname: String,
        lastname: String,
        nickname: String,
        email: String,
        password: String
    ) {
        //_isAuthorizedFlow.emit(true)
        //TODO: Get API response for email availability, change screen to email confirm
    }

    suspend fun verifyCode(
        firstname: String,
        lastname: String,
        nickname: String,
        email: String,
        password: String,
        code: String
    ) {
        _isAuthorizedFlow.emit(true)
        //TODO: Get API response for email availability, change screen to email confirm
    }

    suspend fun logout() {
        _isAuthorizedFlow.emit(false)
    }
}