package com.dapm.appbucal360.data

import android.os.Build
import androidx.annotation.RequiresApi
import com.dapm.appbucal360.model.doctor.Doctor
import com.dapm.appbucal360.model.user.User
import com.dapm.appbucal360.utils.EnumDoctorStatus
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.UUID
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
            val id = UUID.fromString(document.getString("id"))
            val name = document.getString("name") ?: ""
            val lastName = document.getString("lastName") ?: ""
            val workingDays = document.get("workingDays") as? List<String> ?: listOf()
            val startTime = LocalTime.parse(document.getString("startTime"))
            val endTime = LocalTime.parse(document.getString("endTime"))

            val doctor = Doctor(id, name, lastName, workingDays, startTime, endTime)
            doctors.add(doctor)
        }

        return doctors
    }
}
