package com.buffup.sdk.model

import com.squareup.moshi.Json

data class Author(
    @field:Json(name = "first_name")
    val firstName: String,
    @field:Json(name = "image")
    val image: String?,
    @field:Json(name = "last_name")
    val lastName: String
)