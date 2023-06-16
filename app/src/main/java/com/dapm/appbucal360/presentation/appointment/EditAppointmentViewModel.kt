package com.dapm.appbucal360.presentation.appointment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dapm.appbucal360.domain.appointment.EditAppointmentUseCase
import com.dapm.appbucal360.model.appointment.Appointment
import com.dapm.appbucal360.model.appointment.AppointmentState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditAppointmentViewModel @Inject constructor(
    private val editAppointmentUseCase: EditAppointmentUseCase
) : ViewModel() {

    private val _updateResult = MutableLiveData<AppointmentState>()
    val updateResult: LiveData<AppointmentState> get() = _updateResult

    fun updateAppointment(appointment: Appointment) {
        viewModelScope.launch {
            try {
                val updatedAppointment = editAppointmentUseCase(appointment)
                _updateResult.value = AppointmentState.Success(updatedAppointment)
            } catch (e: Exception) {
                _updateResult.value = AppointmentState.Error(e)
            }
        }
    }
}