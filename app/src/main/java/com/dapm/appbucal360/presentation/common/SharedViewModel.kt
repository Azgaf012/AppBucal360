package com.dapm.appbucal360.presentation.common

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dapm.appbucal360.model.appointment.Appointment
import com.dapm.appbucal360.model.user.User
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor() : ViewModel() {
    val loggedInUser = MutableLiveData<User>()
    val appointmentSelected = MutableLiveData<Appointment>()
}