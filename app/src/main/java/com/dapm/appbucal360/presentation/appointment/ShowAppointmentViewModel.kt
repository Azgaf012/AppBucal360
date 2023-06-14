package com.dapm.appbucal360.presentation.appointment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dapm.appbucal360.domain.appointment.DeleteAppointmentUseCase
import com.dapm.appbucal360.domain.appointment.GetAppointmentsUseCase
import com.dapm.appbucal360.model.appointment.Appointment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShowAppointmentViewModel @Inject constructor(
    private val getAppointmentsUseCase: GetAppointmentsUseCase,
    private val deleteAppointmentUseCase: DeleteAppointmentUseCase
) : ViewModel() {

    private val _appointments = MutableLiveData<Result<List<Appointment>>>()
    val appointments: LiveData<Result<List<Appointment>>> get() = _appointments

    private val _deleteResult = MutableLiveData<Result<Unit>>()
    val deleteResult: LiveData<Result<Unit>> get() = _deleteResult

    fun fetchAppointments(userId: String) {
        viewModelScope.launch {
            val result = getAppointmentsUseCase(userId)
            if (result != null) {
                _appointments.value = result
            } else {
                // Manejar el caso en el que result es nulo
            }
        }
    }

    fun deleteAppointment(appointmentId: String) {
        viewModelScope.launch {
            val result = deleteAppointmentUseCase(appointmentId)
            _deleteResult.value = result
        }
    }
}