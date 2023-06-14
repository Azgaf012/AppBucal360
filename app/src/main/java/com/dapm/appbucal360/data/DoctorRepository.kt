package com.dapm.appbucal360.data

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class DoctorRepository @Inject constructor() {
    private val db = Firebase.firestore

    suspend fun getDoctors(): List<String> {
        val querySnapshot = db.collection("doctor")
            .whereEqualTo("estado_doctor", "A")
            .get()
            .await()

        val doctors = mutableListOf<String>()

        for (document in querySnapshot) {
            val nombreDoctor = document.getString("nombre_doctor")
            val apellidoDoctor = document.getString("apellido_doctor")

            if (nombreDoctor != null && apellidoDoctor != null) {
                val doctorFullName = "$nombreDoctor $apellidoDoctor"
                doctors.add(doctorFullName)
            }
        }

        return doctors
    }
}
