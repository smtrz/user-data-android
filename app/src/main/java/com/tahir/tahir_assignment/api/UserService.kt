package com.tahir.tahir_assignment.api

import com.tahir.tahir_assignment.constants.WebServiceConstants
import com.tahir.tahir_assignment.models.data.User
import com.tahir.tahir_assignment.network.ResponseResult
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.GET

/**
 * User Service contains suspend methods for the network calls
 */
interface UserService {
    @GET(WebServiceConstants.GET_USERS)
    suspend fun getAllUsers(): Response<List<User>>
}