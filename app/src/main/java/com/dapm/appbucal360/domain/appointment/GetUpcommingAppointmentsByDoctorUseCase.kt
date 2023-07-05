package com.dapm.appbucal360.domain.appointment

import com.dapm.appbucal360.data.AppointmentRepository
import com.dapm.appbucal360.model.appointment.Appointment
import javax.inject.Inject

class GetUpcommingAppointmentsByDoctorUseCase @Inject constructor(private val appointmentRepository: AppointmentRepository) {
    suspend operator fun invoke(doctorId: String): Result<List<Appointment>> {
        return try {
            appointmentRepository.getUpcomingAppointmentsByDoctor(doctorId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}