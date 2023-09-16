package com.tahir.tahir_assignment.repositories.remote

import com.tahir.tahir_assignment.api.UserService
import javax.inject.Inject


/**
 * all operations related to fetching data from online sources are contained in RemoteDataSource
 * @constructor userService
 */
class RemoteDataSource @Inject constructor(private val userService: UserService) {
    suspend fun getAllUsers() = userService.getAllUsers()

}
