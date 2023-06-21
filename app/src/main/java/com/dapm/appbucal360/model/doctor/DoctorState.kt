package com.dapm.appbucal360.model.doctor

sealed class DoctorState{
    data class Success(val doctor: Result<Doctor>): DoctorState()
    data class Error(val exception: Exception): DoctorState()
}
