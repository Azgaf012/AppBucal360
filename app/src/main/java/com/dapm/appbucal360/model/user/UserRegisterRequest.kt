package com.dapm.appbucal360.model.user

data class UserRegisterRequest(
    val names: String,
    val phoneNumber: String,
    val email: String,
    val birthDate: String,
    val password: String,
    val passwordConfirmation: String
)
