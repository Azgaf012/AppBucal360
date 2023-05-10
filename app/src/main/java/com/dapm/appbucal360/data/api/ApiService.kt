package com.dapm.appbucal360.data.api

import com.dapm.appbucal360.model.user.User
import com.dapm.appbucal360.model.user.LoggedInUser
import com.dapm.appbucal360.model.user.UserLoginResponse
import com.dapm.appbucal360.model.user.UserRegisterRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("auth/login")
    suspend fun loginUser(@Body loggedInUser: LoggedInUser): UserLoginResponse

    @POST("auth/register")
    suspend fun registerUser(@Body userRegisterRequest: UserRegisterRequest): User

}