package com.buffup.sdk.model

import com.squareup.moshi.Json

data class BuffResponse(
    @field:Json(name = "result")
    val result: BuffResult
)