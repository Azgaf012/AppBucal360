package com.dapm.appbucal360.model.user

import java.util.Date

data class User (
    val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val phoneNumber: String = "",
    val email: String = "",
    val birthDate: Date = Date(),
    val role: String = ""
        )