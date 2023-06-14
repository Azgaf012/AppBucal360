package com.dapm.appbucal360.data

import com.dapm.appbucal360.model.user.LoggedInUser
import com.dapm.appbucal360.model.user.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.util.Date
import javax.inject.Inject

class UserRepository @Inject constructor(){

    private val auth = FirebaseAuth.getInstance()
    val db = Firebase.firestore

    suspend fun login(email: String, password: String): Result<User> {
        return runCatching {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val user = result.user
            if (user != null) {
                val doc = db.collection("paciente").document(user.uid).get().await()
                doc.toObject(User::class.java) ?: throw IllegalStateException("User not found in Firestore")
            } else {
                throw IllegalStateException("Firebase Auth user is null")
            }
        }
    }

    suspend fun register(email: String, password: String, firstName: String, lastName: String, phoneNumber: String, birthDate: Date): Result<LoggedInUser> {
        return runCatching {
            // Register the user in Firebase Auth.
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user

            // If the user was successfully registered, save their additional information in Firestore.
            if (user != null) {
                val firestoreUser = User(user.uid, firstName, lastName, phoneNumber, email, birthDate)
                db.collection("paciente").document(user.uid).set(firestoreUser).await()
            }

            LoggedInUser(user?.uid ?: "", user?.email ?: "")
        }
    }
}