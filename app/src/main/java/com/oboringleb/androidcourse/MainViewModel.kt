package com.oboringleb.androidcourse

import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MainViewModel: ViewModel() {
    sealed class ViewState {
        object Loading: ViewState()
        data class Data(val userList: List<User>): ViewState()
    }

    companion object {
        val LOG_TAG = MainViewModel::class.java.simpleName
    }

    private val _viewState = MutableStateFlow<ViewState>(ViewState.Loading)

    val viewState: Flow<ViewState>
        get() = _viewState.asStateFlow()

    init {
        viewModelScope.launch {
            _viewState.emit(ViewState.Loading)
            val users = loadUsers()
            _viewState.emit(ViewState.Data(users))
        }
    }

    private suspend fun loadUsers(): List<User> {
        return withContext(Dispatchers.IO) {
            Log.d(LOG_TAG, "loadUsers()")
            Thread.sleep(3000L)
            provideApi().getUsers().data
        }
    }

    private fun provideApi(): Api {
        return Retrofit
            .Builder()
            .client(provideOkHttpClient())
            .baseUrl("https://reqres.in/api/")
            .addConverterFactory(MoshiConverterFactory.create(provideMoshi()))
            .build()
            .create(Api::class.java)
    }

    private fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().build()
    }

    private fun provideMoshi(): Moshi {
        return Moshi.Builder().build()
    }
}