package com.dapm.appbucal360.model.appointment

sealed class AppointmentState{
    data class Success(val appointment: Result<Appointment>): AppointmentState()
    data class Error(val exception: Exception): AppointmentState()
}
