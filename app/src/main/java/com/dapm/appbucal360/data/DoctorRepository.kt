package com.dapm.appbucal360.data

import com.dapm.appbucal360.model.doctor.Doctor
import com.dapm.appbucal360.utils.EnumDoctorStatus
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.util.*
import javax.inject.Inject

class DoctorRepository @Inject constructor() {
    private val db = Firebase.firestore

    suspend fun getDoctors(): List<Doctor> {
        val querySnapshot = db.collection("doctor")
            .whereEqualTo("status", EnumDoctorStatus.ACTIVED)
            .get()
            .await()

        val doctors = mutableListOf<Doctor>()

        for (document in querySnapshot) {
            val id = document.getString("id")
            val name = document.getString("name") ?: ""
            val lastName = document.getString("lastName") ?: ""
            val workingDays = document.get("workingDays") as? List<String> ?: listOf()
            val startTime = document.getString("startTime")
            val endTime = document.getString("endTime")

            val doctor = Doctor(id, name, lastName, workingDays, startTime, endTime)
            doctors.add(doctor)
        }

        return doctors
    }

    suspend fun registerDoctor(
        name: String, lastName: String, startTime: String, endTime: String, workingDays: List<String>
    ): Result<Doctor> {
        return try {
            val id = UUID.randomUUID()
            val doctor = Doctor(id.toString(), name, lastName, workingDays, startTime, endTime)
            db.collection("doctor").document(id.toString()).set(doctor).await()
            Result.success(doctor)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
