package com.dapm.appbucal360.presentation.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dapm.appbucal360.data.UserRepository
import com.dapm.appbucal360.model.user.RegistrationState
import kotlinx.coroutines.launch
import java.util.Date

class RegisterViewModel : ViewModel() {

    private val userRepository = UserRepository()

    private val _registrationState = MutableLiveData<RegistrationState>()
    val registrationState: LiveData<RegistrationState> = _registrationState

    fun register(email: String, password: String, firstName: String, lastName: String, phoneNumber: String, birthDate: Date) {
        viewModelScope.launch {
            val result = userRepository.register(email, password, firstName, lastName, phoneNumber, birthDate)
            if (result.isSuccess) {
                _registrationState.value = RegistrationState.Success(result.getOrNull()!!)
            } else {
                _registrationState.value = RegistrationState.Error(result.exceptionOrNull())
            }
        }
    }

}