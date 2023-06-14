package com.dapm.appbucal360.domain.doctor

import com.dapm.appbucal360.data.DoctorRepository
import javax.inject.Inject

class GetDoctorsUseCase @Inject constructor(private val repository: DoctorRepository) {
    suspend operator fun invoke(): List<String> {
        return repository.getDoctors()
    }
}