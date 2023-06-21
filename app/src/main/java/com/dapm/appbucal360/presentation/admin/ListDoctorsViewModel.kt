package com.dapm.appbucal360.presentation.admin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dapm.appbucal360.domain.appointment.GetAppointmentsUseCase
import com.dapm.appbucal360.domain.doctor.GetDoctorsUseCase
import com.dapm.appbucal360.model.appointment.Appointment
import com.dapm.appbucal360.model.doctor.Doctor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListDoctorsViewModel @Inject constructor(
    private val getDoctorsUseCase: GetDoctorsUseCase
) : ViewModel() {

    private val _doctorsList = MutableLiveData<List<Doctor>>()
    val doctorsList: LiveData<List<Doctor>> get() = _doctorsList

    fun fetchDoctors() {
        viewModelScope.launch {
            try {
                val doctors = getDoctorsUseCase()
                _doctorsList.value = doctors
            } catch (e: Exception) {
                // handle error
            }
        }
    }

}