package com.buffup.sdk.network

import com.buffup.sdk.model.BuffResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface BuffApi {

    @GET("/buffs/{buffId}")
    suspend fun loadBuff(@Path("buffId") id: Int): BuffResponse
}