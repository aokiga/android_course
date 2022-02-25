package com.oboringleb.androidcourse.data.network

import com.oboringleb.androidcourse.data.network.response.GetUsers
import retrofit2.http.GET

interface Api {
    @GET("users?per_page=10")
    suspend fun getUsers(): GetUsers
}