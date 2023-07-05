package com.dapm.appbucal360.presentation.admin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dapm.appbucal360.domain.doctor.EnableDoctorUseCase
import com.dapm.appbucal360.domain.doctor.GetAllDoctorsUseCase
import com.dapm.appbucal360.model.doctor.Doctor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListDoctorsViewModel @Inject constructor(
    private val getAllDoctorsUseCase: GetAllDoctorsUseCase,
    private val enableDoctorUseCase: EnableDoctorUseCase
) : ViewModel() {

    private val _doctorsList = MutableLiveData<List<Doctor>>()
    val doctorsList: LiveData<List<Doctor>> get() = _doctorsList


    private val _updatedDoctor = MutableLiveData<Result<Doctor>>()
    val updatedDoctor: LiveData<Result<Doctor>> get() = _updatedDoctor

    fun fetchDoctors() {
        viewModelScope.launch {
            try {
                val doctors = getAllDoctorsUseCase()
                _doctorsList.value = doctors
            } catch (e: Exception) {
                // handle error
            }
        }
    }

    fun enableDoctor(id: String){
        viewModelScope.launch {
            try {
                val result = enableDoctorUseCase(id)
                if (result.isSuccess) {
                    _updatedDoctor.value = result
                }
            } catch (e: Exception) {
                // handle error
            }
        }
    }

}