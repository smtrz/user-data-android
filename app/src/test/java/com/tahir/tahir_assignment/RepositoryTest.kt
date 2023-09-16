package com.tahir.tahir_assignment

import com.tahir.tahir_assignment.api.UserService
import com.tahir.tahir_assignment.models.data.User
import com.tahir.tahir_assignment.network.ResponseResult
import com.tahir.tahir_assignment.repositories.Repository
import com.tahir.tahir_assignment.repositories.remote.RemoteDataSource
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class RepositoryTest {
    private lateinit var repository: Repository
    private lateinit var userService: UserService

    @Before
    fun setup() {
        userService = mockk()
        val remoteDataSource = RemoteDataSource(userService)
        repository = Repository(remoteDataSource)
    }

    @Test
    fun `getAllUsers should return Success`() = runBlocking {
        val dummyUsers = listOf(
            User(
                id = 1,
                name = "Tahir Raza",
                username = "smtrz110",
                email = "smtrz@yahoo.com",
                address = mapOf(
                    "street" to "123 Main St",
                    "suite" to "Apt 101",
                    "city" to "Some City",
                    "zipcode" to "12345",
                    "geo" to mapOf(
                        "lat" to "12.3456",
                        "lng" to "78.9101"
                    )
                ),
                phone = "555-1234",
                website = "https://smtrz.github.com",
                company = mapOf(
                    "name" to "ABC Inc.",
                    "catchPhrase" to "Life is all about second chance..",
                    "bs" to "this is some bs"
                )
            )
        )
        val mockResponse = Response.success(dummyUsers)
        // Arrange
        coEvery { userService.getAllUsers() } returns mockResponse
        // Assert
        repository.getAllUsers().collect {
            if (it is ResponseResult.Success) {
                assertEquals(dummyUsers, it.data)
            }

        }


    }

    @Test
    fun `getAllUsers should return Error`() = runBlocking {
        // Arrange
        val errorMessage = "Error message"
        val errorCode = 400

        val errorResponseBody = mockk<ResponseBody> {
            every { string() } returns errorMessage
            every { contentType() } returns "application/json".toMediaType()
            every { contentLength() } returns 123L
        }


        val mockResponse = Response.error<List<User>>(errorCode, errorResponseBody)

        coEvery { userService.getAllUsers() } returns mockResponse

        // Act
        val result = repository.getAllUsers()

        // Assert
        result.collect {
            if (it is ResponseResult.Error) {
                assertEquals(it.errmessage, errorMessage)

            }
        }
    }
}