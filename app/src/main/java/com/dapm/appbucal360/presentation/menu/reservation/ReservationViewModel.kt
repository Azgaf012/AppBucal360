package com.dapm.appbucal360.presentation.menu.reservation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dapm.appbucal360.data.AppointmentRepository
import com.dapm.appbucal360.model.appointment.AppointmentState
import kotlinx.coroutines.launch
import java.util.Date

class ReservationViewModel : ViewModel() {

    private val appointmentRepository = AppointmentRepository()

    private val _registerResult = MutableLiveData<AppointmentState>()
    val registerResult: LiveData<AppointmentState> get() = _registerResult

    fun registerAppointment(doctor: String, date: Date) {
        viewModelScope.launch {
            try {
                val appointment = appointmentRepository.registerAppointment(doctor, date)
                _registerResult.value = AppointmentState.Success(appointment)
            } catch (e: Exception) {
                _registerResult.value = AppointmentState.Error(e)
            }
        }
    }

}