package com.dapm.appbucal360.domain.appointment

import com.dapm.appbucal360.data.AppointmentRepository
import com.dapm.appbucal360.model.appointment.Appointment
import com.dapm.appbucal360.model.user.User
import java.util.Date
import javax.inject.Inject

class RegisterAppointmentUseCase @Inject constructor(private val appointmentRepository: AppointmentRepository) {

    suspend operator fun invoke(
        doctor: String,
        date: Date,
        patient: User,
        time: String
    ): Result<Appointment> {
        return try {
            appointmentRepository.registerAppointment(doctor, date, patient, time)
        } catch (e: Exception) {
            throw e
        }
    }

}