package com.buffup.sdk.model

import com.squareup.moshi.Json

data class Answer(
    @field:Json(name = "buff_id")
    val buffId: Int,
    @field:Json(name = "id")
    val id: Long,
    @field:Json(name = "image")
    val image: Map<String, Image>,
    @field:Json(name = "title")
    val title: String
)