package com.buffup.sdk.network

import com.buffup.sdk.model.Buff
import com.buffup.sdk.model.BuffResponse
import com.buffup.sdk.utils.BASE_URL
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.IOException
import java.net.HttpURLConnection

object BuffApiService {

    private val logger =
        HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }

    private val client = OkHttpClient.Builder()
        .addInterceptor(logger)
        .build()

    var buffApi: BuffApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
        .create(BuffApi::class.java)

    private suspend fun <T> safeApiCall(
        dispatcher: CoroutineDispatcher,
        apiCall: suspend () -> T
    ): Result<T> {
        return withContext(dispatcher) {
            try {
                Result.Success(apiCall.invoke())
            } catch (throwable: Throwable) {
                when (throwable) {
                    is IOException -> Result.Error(error = BuffError.IOError(throwable.message))
                    is HttpException -> {
                        val code = throwable.code()
                        val errorResponse = convertErrorBody(throwable)
                        Result.Error(code, errorResponse)
                    }
                    else -> {
                        Result.Error(null, BuffError.GenericError(throwable = throwable))
                    }
                }
            }
        }
    }

    private fun convertErrorBody(throwable: HttpException): BuffError? = when (throwable.code()) {
        HttpURLConnection.HTTP_CLIENT_TIMEOUT,
        HttpURLConnection.HTTP_GATEWAY_TIMEOUT -> BuffError.TimeoutError(throwable = throwable)
        HttpURLConnection.HTTP_NOT_FOUND -> BuffError.BuffNotFound(
            HttpURLConnection.HTTP_UNAUTHORIZED,
            throwable.message()
        )
        else -> BuffError.HttpError(throwable.code(), throwable.message())
    }

    suspend fun getBuff(id: Int): Buff {
        val response = safeApiCall(Dispatchers.IO) {
            buffApi.loadBuff(id = id)
        }
        return when (response) {
            is Result.Success -> Buff(buffResult = response.data.result)
            is Result.Error -> Buff(buffError = response.error)
            else -> Buff()
        }
    }

}
