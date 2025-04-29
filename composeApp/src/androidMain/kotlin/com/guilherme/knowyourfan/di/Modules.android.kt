package com.guilherme.knowyourfan.di

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.guilherme.knowyourfan.knowyourfan.data.remote.firebase.FirebaseAuthentication
import com.guilherme.knowyourfan.knowyourfan.data.remote.firebase.FirebaseAuthenticationImpl
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformModule(activity: Any): Module = module {

    single<FirebaseAuth> { Firebase.auth }

    single<FirebaseFirestore> { Firebase.firestore }

    single<FirebaseAuthentication> {
        FirebaseAuthenticationImpl(auth = get(), db = get())
    }

}