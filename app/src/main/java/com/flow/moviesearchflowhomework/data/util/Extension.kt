package com.flow.moviesearchflowhomework.data.util

import android.util.Log
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

suspend inline fun <T> catchingApiCall(crossinline apiCall: suspend () -> Response<T>): T? {
    return runCatching {
        apiCall().body()
    }.getOrElse { throwable ->
        when (throwable) {
            is IOException -> {         //SocketConnection, Timeout Error Catch, 보통 핸드쉐이크 관련
                Log.e("IOException", throwable.message.toString())
                null
            }
            is HttpException -> {       //error 코드까지는 내려옴, end point 까지는 접근
                Log.e("HttpException", throwable.message.toString())
                null
            }
            else -> {
                null
            }
        }
    }
}