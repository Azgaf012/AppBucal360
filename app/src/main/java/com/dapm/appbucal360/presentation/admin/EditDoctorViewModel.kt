package com.dapm.appbucal360.presentation.admin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dapm.appbucal360.domain.doctor.EditDoctorUseCase
import com.dapm.appbucal360.model.doctor.Doctor
import com.dapm.appbucal360.model.doctor.DoctorState
import com.dapm.appbucal360.utils.EnumDoctorStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditDoctorViewModel @Inject constructor(
    private val editDoctorUseCase: EditDoctorUseCase
) : ViewModel() {

    private val _updateResult = MutableLiveData<DoctorState>()
    val updateResult: LiveData<DoctorState> get() = _updateResult

    fun updateDoctor(id: String,
                     name: String,
                     lastName: String,
                     startTime: String,
                     endTime: String,
                     workingDays: List<String>,
                     status: String){
        viewModelScope.launch {
            try{
                val doctor = Doctor(id, name, lastName, workingDays, startTime, endTime, status)
                val updateDoctor = editDoctorUseCase(doctor)
                _updateResult.value = DoctorState.Success(updateDoctor)
            } catch (e: Exception) {
                _updateResult.value = DoctorState.Error(e)
            }
        }
    }
}