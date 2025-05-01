package com.guilherme.knowyourfan.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.guilherme.knowyourfan.MainActivity
import com.guilherme.knowyourfan.knowyourfan.data.local.datastore.createDataStore
import com.guilherme.knowyourfan.knowyourfan.data.local.sqldelight.DriverFactory
import com.guilherme.knowyourfan.knowyourfan.data.remote.firebase.FirebaseAuthentication
import com.guilherme.knowyourfan.knowyourfan.data.remote.firebase.FirebaseAuthenticationImpl
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformModule(activity: Any): Module = module {

    single<FirebaseAuth> { Firebase.auth }

    single<FirebaseFirestore> { Firebase.firestore }

    single<DriverFactory> { DriverFactory(activity as Context) }

    single<DataStore<Preferences>> { createDataStore(activity as Context) }

    single<FirebaseAuthentication> {
        FirebaseAuthenticationImpl(auth = get(), db = get(), activity = activity as MainActivity)
    }

}