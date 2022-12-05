package com.faheem.currencyconverstion.data.remote.base

import com.google.gson.stream.MalformedJsonException
import org.json.JSONObject
import retrofit2.Response

open class BaseApi {

    suspend fun <T> executeApi(call: suspend () -> Response<T>): Result<T> {
        try {
            val response = call.invoke()
            if (response.isSuccessful)
                return Result.success(response.body()!!)

            return Result.failure(Exception(parseError(response)))

        } catch (exception: MalformedJsonException) {
            return Result.failure(exception)
        } catch (ex: Exception) {
            return Result.failure(ex)
        }
    }

    private fun <T> parseError(response: Response<T>): String {
        val errorBody = response.errorBody()?.string()
        errorBody?.let {
            try {
                val bodyObj = JSONObject(it)
                return bodyObj.getString("description")
            } catch (ex: Exception) {
                return "Something went wrong"
            }
        } ?: return "Something went wrong"

    }
}