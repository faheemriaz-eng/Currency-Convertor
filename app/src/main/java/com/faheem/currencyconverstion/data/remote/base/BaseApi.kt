package com.faheem.currencyconverstion.data.remote.base

import com.google.gson.stream.MalformedJsonException
import retrofit2.Response

open class BaseApi {

    suspend fun <T> executeApi(call: suspend () -> Response<T>): Result<T> {
        try {
            val response = call.invoke()
            if (response.isSuccessful)
                return Result.success(response.body()!!)

            return Result.failure(Exception(response.errorBody()?.string()))

        } catch (exception: MalformedJsonException) {
            return Result.failure(exception)
        } catch (exception: Exception) {
            return Result.failure(exception)
        }
    }
}