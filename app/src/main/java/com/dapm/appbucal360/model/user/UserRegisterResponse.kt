package com.dapm.appbucal360.model.user

data class UserRegisterResponse(
    val id: Int,
    val names: String,
    val phoneNumber: String,
    val email: String,
    val birthDate: String
)
