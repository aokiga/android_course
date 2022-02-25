package com.oboringleb.androidcourse.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class User(
    @Json(name = "avatar") val avatarURL: String,
    @Json(name = "first_name") val userName: String,
    @Json(name = "email") val groupName: String,
)
