package com.dapm.appbucal360.domain.doctor

import com.dapm.appbucal360.data.DoctorRepository
import com.dapm.appbucal360.model.doctor.Doctor
import javax.inject.Inject

class DisableDoctorUseCase @Inject constructor(
    private val doctorRepository: DoctorRepository
){

    suspend operator fun invoke(id: String): Result<Doctor>{
        return try{
            doctorRepository.enableDoctor(id)
        }catch (e: Exception){
            throw e
        }
    }

}