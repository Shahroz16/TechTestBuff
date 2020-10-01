package com.buffup.sdk.network

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class Result<out T> {

    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val code: Int? = null, val error: BuffError? = null) : Result<Nothing>()
    object Loading : Result<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$error]"
            Loading -> "Loading"
        }
    }
}

/**
 * `true` if [Result] is of type [Success] & holds non-null [Success.data].
 */
val Result<*>.succeeded
    get() = this is Result.Success && data != null


/**
 * Contains the possible errors
 */
sealed class BuffError {
    data class IOError(val errorMsg: String?) : BuffError()
    data class HttpError(val errorCode: Int, val errorMsg: String?) : BuffError()
    data class BuffNotFound(val errorCode: Int = 404, val errorMsg: String?) : BuffError()
    data class TimeoutError(val errorMsg: String = "Timeout", val throwable: Throwable) :
        BuffError()

    data class GenericError(val throwable: Throwable) : BuffError()
}