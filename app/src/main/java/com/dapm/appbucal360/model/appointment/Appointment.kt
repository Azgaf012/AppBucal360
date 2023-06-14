package com.dapm.appbucal360.model.appointment

import com.dapm.appbucal360.model.user.User
import java.util.*

data class Appointment(
    var id: String? = null,
    var date: Date? = null,
    var time: String? = null,
    var patient: User? = null,
    var doctor: String? = null,
    var status: String? = null
) {
    // Constructor sin argumentos requerido para la deserialización de Firebase
    constructor() : this(null, null, null, null, null, null)
}