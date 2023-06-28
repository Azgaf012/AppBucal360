package com.dapm.appbucal360.domain.doctor

import com.dapm.appbucal360.data.DoctorRepository
import com.dapm.appbucal360.model.doctor.Doctor
import javax.inject.Inject

class GetAllDoctorsUseCase @Inject constructor(private val repository: DoctorRepository) {

    suspend operator fun invoke(): List<Doctor> {
        return repository.getAllDoctors()
    }
}