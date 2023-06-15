package com.dapm.appbucal360.domain.doctor

import android.os.Build
import androidx.annotation.RequiresApi
import com.dapm.appbucal360.data.DoctorRepository
import com.dapm.appbucal360.model.doctor.Doctor
import javax.inject.Inject

class GetDoctorsUseCase @Inject constructor(private val repository: DoctorRepository) {

    suspend operator fun invoke(): List<Doctor> {
        return repository.getDoctors()
    }
}