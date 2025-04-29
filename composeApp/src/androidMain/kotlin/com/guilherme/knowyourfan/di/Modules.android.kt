package com.guilherme.knowyourfan.di

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import com.guilherme.knowyourfan.MainActivity
import com.guilherme.knowyourfan.knowyourfan.data.remote.firebase.FirebaseAuthentication
import com.guilherme.knowyourfan.knowyourfan.data.remote.firebase.FirebaseAuthenticationImpl
import com.guilherme.knowyourfan.knowyourfan.data.remote.firebase.FirebaseStorageImpl
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformModule(activity: Any): Module = module {

    single<FirebaseStorage> {
        Firebase.storage
    }

    single<FirebaseAuth> { Firebase.auth }

    single<FirebaseAuthentication> {
        FirebaseAuthenticationImpl(auth = get())
    }

    single<com.guilherme.knowyourfan.knowyourfan.data.remote.firebase.FirebaseStorage> {
        FirebaseStorageImpl(
            storage = get()
        )
    }

}