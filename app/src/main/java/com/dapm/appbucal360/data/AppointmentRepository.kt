package com.dapm.appbucal360.data

import com.dapm.appbucal360.model.appointment.Appointment
import com.dapm.appbucal360.model.user.LoggedInUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.util.*

class AppointmentRepository {
    val db = Firebase.firestore

    suspend fun registerAppointment(doctor: String, date: Date): Result<Appointment> {
        return try {
            val id = UUID.randomUUID()
            val appointment = Appointment(id.toString(), date, doctor)
            db.collection("citas").document(id.toString()).set(appointment).await()
            Result.success(appointment)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}