package com.tahir.tahir_assignment

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tahir.tahir_assignment.models.data.User
import com.tahir.tahir_assignment.network.ResponseResult
import com.tahir.tahir_assignment.repositories.Repository
import com.tahir.tahir_assignment.viewmodel.UsersViewModel
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class UsersViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()
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

    // LiveData updates happen immediately on the same thread.
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    // Mock repository
    private val repository: Repository = mockk()

    // Mock LiveData observer
    private val loadingObserver: Observer<Boolean> = mockk(relaxUnitFun = true)
    private val usersObserver: Observer<List<User>?> = mockk(relaxUnitFun = true)

    // ViewModel to be tested
    private lateinit var viewModel: UsersViewModel

    @Before
    fun setUp() {
        viewModel = UsersViewModel(repository)
        viewModel.isEventLoading.observeForever(loadingObserver)
        viewModel.usersLiveData.observeForever(usersObserver)
    }

    @Test
    fun `getAllUsers should update LiveData on success`() = runBlocking {
        coEvery { repository.getAllUsers() } answers { flowOf(ResponseResult.Success(dummyUsers)) }

        // Act
        viewModel.getAllUsers()

        // Assert
        verify { loadingObserver.onChanged(false) }
        verify { usersObserver.onChanged(dummyUsers) }
    }

    @Test
    fun `getAllUsers should update LiveData on error with null`() = runBlocking {
        coEvery { repository.getAllUsers() } returns flowOf(ResponseResult.Error("Error occured"))

        // Act
        viewModel.getAllUsers()

        // Assert
        verify { loadingObserver.onChanged(false) }
        verify { usersObserver.onChanged(null) }
    }
}
