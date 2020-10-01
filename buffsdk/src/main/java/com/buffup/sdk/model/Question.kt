package com.buffup.sdk.model

import com.squareup.moshi.Json

data class Question(
    @field:Json(name = "category")
    val category: Int,
    @field:Json(name = "id")
    val id: Int,
    @field:Json(name = "title")
    val title: String
)