package com.dapm.appbucal360.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dapm.appbucal360.data.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val userRepository = UserRepository()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val result = userRepository.login(email, password)
            result.fold(
                onSuccess = { loggedInUser ->
                    // Manejar caso de éxito
                },
                onFailure = { exception ->
                    // Manejar caso de error
                }
            )
        }
    }

}