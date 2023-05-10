package com.dapm.appbucal360.data

import com.dapm.appbucal360.model.user.LoggedInUser
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class UserRepository{

    private val auth = FirebaseAuth.getInstance()

    suspend fun login(email: String, password: String): Result<LoggedInUser> {
        return runCatching {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val user = result.user
            LoggedInUser(user?.uid ?: "", user?.email ?: "")
        }
    }
}