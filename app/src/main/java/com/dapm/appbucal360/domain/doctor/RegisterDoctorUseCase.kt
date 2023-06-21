package com.dapm.appbucal360.domain.doctor

import com.dapm.appbucal360.data.DoctorRepository
import com.dapm.appbucal360.model.doctor.Doctor
import javax.inject.Inject

class RegisterDoctorUseCase @Inject constructor(private val doctorRepository: DoctorRepository) {

    suspend operator fun invoke(
        name: String,
        lastName: String,
        startTime: String,
        endTime: String,
        workingDays: List<String>
    ): Result<Doctor> {
        return try {
            doctorRepository.registerDoctor(name, lastName, startTime, endTime, workingDays)
        } catch (e: Exception) {
            throw e
        }
    }

}