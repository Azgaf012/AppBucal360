package com.dapm.appbucal360.presentation.appointment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dapm.appbucal360.domain.appointment.RegisterAppointmentUseCase
import com.dapm.appbucal360.domain.doctor.GetDoctorsActivedUseCase
import com.dapm.appbucal360.model.appointment.AppointmentState
import com.dapm.appbucal360.model.doctor.Doctor
import com.dapm.appbucal360.model.user.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReserveAppointmentViewModel @Inject constructor(
    private val registerAppointmentUseCase: RegisterAppointmentUseCase,
    private val getDoctorsActivedUseCase: GetDoctorsActivedUseCase
) : ViewModel() {

    private val _registerResult = MutableLiveData<AppointmentState>()
    val registerResult: LiveData<AppointmentState> get() = _registerResult

    private val _doctorsList = MutableLiveData<List<Doctor>>()
    val doctorsList: LiveData<List<Doctor>> get() = _doctorsList

    fun registerAppointment(doctor: Doctor, date: String, patient: User, time: String) {
        viewModelScope.launch {
            try {
                val appointment = registerAppointmentUseCase(doctor, date, patient, time)
                _registerResult.value = AppointmentState.Success(appointment)
            } catch (e: Exception) {
                _registerResult.value = AppointmentState.Error(e)
            }
        }
    }

    fun fetchDoctors() {
        viewModelScope.launch {
            try {
                val doctors = getDoctorsActivedUseCase()
                _doctorsList.value = doctors
            } catch (e: Exception) {
                // handle error
            }
        }
    }

}