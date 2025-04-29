package com.guilherme.knowyourfan.knowyourfan.data.remote.firebase

import com.guilherme.knowyourfan.domain.AuthenticationError
import com.guilherme.knowyourfan.domain.Result

interface FirebaseAuthentication {

    suspend fun signUpUser(email: String, password: String): Result<Unit, AuthenticationError.Authentication>

}