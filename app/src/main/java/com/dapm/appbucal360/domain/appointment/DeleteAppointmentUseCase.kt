package com.dapm.appbucal360.domain.appointment

import com.dapm.appbucal360.data.AppointmentRepository
import javax.inject.Inject

class DeleteAppointmentUseCase @Inject constructor(private val appointmentRepository: AppointmentRepository) {
    suspend operator fun invoke(appointmentId: String): Result<Unit> {
        return appointmentRepository.deleteAppointment(appointmentId)
    }
}