package com.tahir.tahir_assignment.network

import retrofit2.Response
import timber.log.Timber

/**
 * abstract class with method for calling the API and return the response wrapped in sealed class of
 * ResponseResult
 */
abstract class BaseApiResponse {

    suspend fun <T : Any> safeApiCall(apiCall: suspend () -> Response<T>): ResponseResult<T> {
        try {

            val response = apiCall()

            if (response.isSuccessful) {
                val body = response.body()
                body?.let {
                    Timber.d("API call successful")
                    return ResponseResult.Success(body)
                }
            }
            Timber.e(
                "API call failed. Error: ${
                    response.errorBody()?.string() ?: response.message()
                }"
            )
            return ResponseResult.Error(response.errorBody()?.string() ?: response.message())
        } catch (e: Exception) {
            Timber.e("Exception during API call: ${e.message ?: e.toString()}")
            return ResponseResult.Error(e.message ?: e.toString())
        }
    }


}