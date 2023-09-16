package com.tahir.tahir_assignment.repositories

import com.tahir.tahir_assignment.models.data.User
import com.tahir.tahir_assignment.extensions.applyCommonSideEffects
import com.tahir.tahir_assignment.network.BaseApiResponse
import com.tahir.tahir_assignment.network.ResponseResult
import com.tahir.tahir_assignment.repositories.remote.RemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository is the Single Source of truth that contains instance of remote data
 * source inherits from BaseApiResponse.
 * @constructor remoteDataSource (using construction injection)
 */
@Singleton
class Repository
@Inject
constructor(
    private val remoteDataSource: RemoteDataSource,
) : BaseApiResponse() {

    // get All the users from remote data source operations
    suspend fun getAllUsers(): Flow<ResponseResult<List<User>>> {
        return flow {
            Timber.d("Fetching all users from remote data source...")
            emit(safeApiCall { remoteDataSource.getAllUsers() })
        }.applyCommonSideEffects().catch {
            val errorMessage = it.message ?: "Unknown error occurred"
            emit(ResponseResult.Error(errorMessage))
            Timber.e("Error fetching users: $errorMessage")
        }
    }
}