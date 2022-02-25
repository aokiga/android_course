package com.oboringleb.androidcourse.data.network.response

import com.oboringleb.androidcourse.entity.User
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GetUsers(
    @Json(name = "data") val data: List<User>
)