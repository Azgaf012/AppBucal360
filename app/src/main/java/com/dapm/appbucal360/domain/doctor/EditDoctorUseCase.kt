package com.dapm.appbucal360.domain.doctor

import com.dapm.appbucal360.data.DoctorRepository
import com.dapm.appbucal360.model.doctor.Doctor
import javax.inject.Inject

class EditDoctorUseCase @Inject constructor(
    private val doctorRepository: DoctorRepository
){

    suspend operator fun invoke(doctor: Doctor): Result<Doctor>{
        return try{
            doctorRepository.editDoctor(doctor)
        }catch (e: Exception){
            throw e
        }
    }

}