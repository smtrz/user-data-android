package com.tahir.tahir_assignment.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tahir.tahir_assignment.models.data.User
import com.tahir.tahir_assignment.network.ResponseResult
import com.tahir.tahir_assignment.repositories.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(private val repository: Repository) :
    ViewModel() {
    private var fetchUsersJob: Job? = null
    val isEventLoading = MutableLiveData(false)
    private val _usersLiveData = MutableLiveData<List<User>>()
    val usersLiveData: LiveData<List<User>> get() = _usersLiveData

    init {
        Timber.d("UsersViewModel initialized")
    }

    fun getAllUsers() {
        fetchUsersJob?.cancel()
        viewModelScope.launch(Dispatchers.Default) {
            Timber.d("Getting all users...")
            repository.getAllUsers().collect {
                when (it) {
                    is ResponseResult.Success -> {
                        isEventLoading.postValue(false)
                        _usersLiveData.postValue(it.data)
                        Timber.d("Users retrieved successfully")

                    }

                    is ResponseResult.Progress -> {
                        isEventLoading.postValue(it.isLoading)
                        Timber.d("Loading users: ${it.isLoading}")

                    }

                    is ResponseResult.Error -> {
                        isEventLoading.postValue(false)
                        _usersLiveData.postValue(null)
                        Timber.e("Error while getting users: ${it.errmessage}")

                    }

                }
            }
        }
    }
}