package com.dapm.appbucal360.data

import com.dapm.appbucal360.model.appointment.Appointment
import com.dapm.appbucal360.model.user.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.util.*
import javax.inject.Inject

class AppointmentRepository @Inject constructor() {
    val db = Firebase.firestore

    suspend fun registerAppointment(
        doctor: String, date: Date, patient: User, time: String
    ): Result<Appointment> {
        return try {
            val id = UUID.randomUUID()
            val appointment = Appointment(id.toString(), date, doctor, patient, time)
            db.collection("citas").document(id.toString()).set(appointment).await()
            Result.success(appointment)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAppointmentsByUser(userId: String): Result<List<Appointment>> {
        return try {
            val querySnapshot = db.collection("citas")
                .whereEqualTo("patient.id", userId)
                .get()
                .await()

            val appointments = querySnapshot.documents.mapNotNull { document ->
                document.toObject(Appointment::class.java)
            }

            Result.success(appointments)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteAppointment(appointmentId: String): Result<Unit> {
        return try {
            db.collection("citas").document(appointmentId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}