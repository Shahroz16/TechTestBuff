package com.buffup.sdk.model

import com.squareup.moshi.Json

data class BuffResult(
    @field:Json(name = "answers")
    val answers: List<Answer>,
    @field:Json(name = "author")
    val author: Author,
    @field:Json(name = "client_id")
    val clientId: Int,
    @field:Json(name = "created_at")
    val createdAt: String,
    @field:Json(name = "id")
    val id: Int,
    @field:Json(name = "language")
    val language: String,
    @field:Json(name = "priority")
    val priority: Int,
    @field:Json(name = "question")
    val question: Question,
    @field:Json(name = "stream_id")
    val streamId: Int,
    @field:Json(name = "time_to_show")
    val timeToShow: Int
)