package com.guilherme.knowyourfan.knowyourfan.data.remote.firebase

import com.guilherme.knowyourfan.core.domain.UserCheckError
import com.guilherme.knowyourfan.domain.AuthenticationError
import com.guilherme.knowyourfan.domain.Result

interface FirebaseAuthentication {

    suspend fun signUpUser(
        email: String,
        password: String,
        purchases: List<String>,
        events: List<String>,
        interestGames: List<String>,
    ): Result<Unit, AuthenticationError.SignUp>

    suspend fun signInUser(email: String, password: String): Result<Unit, AuthenticationError.SignIn>

    suspend fun isAccountLinkedToX(): Result<Boolean, UserCheckError.User>

}