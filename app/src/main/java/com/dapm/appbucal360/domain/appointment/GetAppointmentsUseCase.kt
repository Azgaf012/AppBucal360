package com.dapm.appbucal360.domain.appointment

import com.dapm.appbucal360.data.AppointmentRepository
import com.dapm.appbucal360.model.appointment.Appointment
import javax.inject.Inject

class GetAppointmentsUseCase @Inject constructor(private val appointmentRepository: AppointmentRepository) {
    suspend operator fun invoke(userId: String): Result<List<Appointment>> {
        return try {
            appointmentRepository.getAppointmentsByUser(userId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}