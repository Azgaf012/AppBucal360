package com.dapm.appbucal360.model.appointment

import com.dapm.appbucal360.model.user.User
import com.dapm.appbucal360.utils.EnumAppointmentStatus
import java.util.*

data class Appointment(
    var id: String? = null,
    var date: Date? = null,
    var doctor: String? = null,
    var patient: User? = null,
    var time: String? = null,
    var status: EnumAppointmentStatus = EnumAppointmentStatus.REGISTERED
) {
    // Constructor sin argumentos requerido para la deserialización de Firebase
    constructor() : this(null, null, null, null, null, EnumAppointmentStatus.REGISTERED)

    fun cancel() {
        status = EnumAppointmentStatus.CANCELLED
    }

    fun attend() {
        status = EnumAppointmentStatus.ATTENDED
    }

}