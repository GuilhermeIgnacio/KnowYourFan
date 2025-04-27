package com.guilherme.knowyourfan.di

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.guilherme.knowyourfan.MainActivity
import com.guilherme.knowyourfan.knowyourfan.data.remote.firebase.FirebaseAuthentication
import com.guilherme.knowyourfan.knowyourfan.data.remote.firebase.FirebaseAuthenticationImpl
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

/*actual val platformModule: Module
    get() = module {
        single<FirebaseAuthentication> {
            FirebaseAuthenticationImpl(
                context = androidContext(),
                auth = Firebase.auth
            )
        }
    }*/

actual fun platformModule(activity: Any): Module = module {

    single<FirebaseAuthentication> {
        FirebaseAuthenticationImpl(
            auth = Firebase.auth,
            activity = activity as MainActivity
        )
    }
}