package com.dapm.appbucal360.data

import com.dapm.appbucal360.model.appointment.Appointment
import com.dapm.appbucal360.model.doctor.Doctor
import com.dapm.appbucal360.model.user.User
import com.dapm.appbucal360.utils.EnumAppointmentStatus
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class AppointmentRepository @Inject constructor() {
    val db = Firebase.firestore

    suspend fun registerAppointment(
        doctor: Doctor, date: String, patient: User, time: String
    ): Result<Appointment> {
        return try {
            val id = UUID.randomUUID()
            val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val parsedDate = format.parse(date)
            val appointment = Appointment(id.toString(), parsedDate, doctor, patient, time)
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
                .whereEqualTo("status",EnumAppointmentStatus.REGISTERED)
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
            db.collection("citas").document(appointmentId).update("status",EnumAppointmentStatus.CANCELLED).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}