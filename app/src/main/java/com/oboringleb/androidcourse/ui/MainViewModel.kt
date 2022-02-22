package com.oboringleb.androidcourse.ui

import com.oboringleb.androidcourse.repository.AuthRepository
import com.oboringleb.androidcourse.ui.base.BaseViewModel
import kotlinx.coroutines.flow.Flow

class MainViewModel: BaseViewModel() {
    val isAuthorizedFlow: Flow<Boolean> = AuthRepository.isAuthorizedFlow
}