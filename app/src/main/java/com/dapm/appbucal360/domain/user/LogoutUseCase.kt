package com.dapm.appbucal360.domain.user

import com.dapm.appbucal360.data.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LogoutUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend operator fun invoke(): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            userRepository.logout()
        }
    }
}