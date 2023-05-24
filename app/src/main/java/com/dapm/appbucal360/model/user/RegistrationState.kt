package com.dapm.appbucal360.model.user

sealed class RegistrationState{
    data class Success(val user: LoggedInUser): RegistrationState()
    data class Error(val exception: Throwable?): RegistrationState()
}
