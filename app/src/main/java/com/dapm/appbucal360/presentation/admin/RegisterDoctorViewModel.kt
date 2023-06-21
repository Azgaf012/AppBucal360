package com.dapm.appbucal360.presentation.admin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dapm.appbucal360.domain.doctor.RegisterDoctorUseCase
import com.dapm.appbucal360.model.doctor.DoctorState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterDoctorViewModel @Inject constructor(
    private val registerDoctorUseCase: RegisterDoctorUseCase
) : ViewModel() {

    private val _registerResult = MutableLiveData<DoctorState>()
    val registerResult: LiveData<DoctorState> get() = _registerResult

    fun registerDoctor(name: String,
                       lastName: String,
                       startTime: String,
                       endTime: String,
                       workingDays: List<String>) {
        viewModelScope.launch {
            try {
                val doctor = registerDoctorUseCase(name, lastName, startTime, endTime, workingDays)
                _registerResult.value = DoctorState.Success(doctor)
            } catch (e: Exception) {
                _registerResult.value = DoctorState.Error(e)
            }
        }
    }

}