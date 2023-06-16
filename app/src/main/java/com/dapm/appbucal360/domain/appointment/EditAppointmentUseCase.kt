package com.dapm.appbucal360.domain.appointment

import com.dapm.appbucal360.data.AppointmentRepository
import com.dapm.appbucal360.model.appointment.Appointment
import javax.inject.Inject

class EditAppointmentUseCase @Inject constructor(
    private val appointmentRepository: AppointmentRepository
) {
    suspend operator fun invoke(appointment: Appointment): Result<Appointment> {
        return try {
            appointmentRepository.editAppointment(appointment)
        } catch (e: Exception) {
            throw e
        }
    }
}