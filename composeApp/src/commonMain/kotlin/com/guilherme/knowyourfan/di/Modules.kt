package com.guilherme.knowyourfan.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.guilherme.knowyourfan.knowyourfan.data.local.datastore.DataStoreImpl
import com.guilherme.knowyourfan.knowyourfan.data.local.sqldelight.RecommendationImpl
import com.guilherme.knowyourfan.knowyourfan.data.remote.api.gemini.GeminiService
import com.guilherme.knowyourfan.knowyourfan.data.remote.api.gemini.GeminiImpl
import com.guilherme.knowyourfan.knowyourfan.domain.DataStoreRepository
import com.guilherme.knowyourfan.knowyourfan.domain.RecommendationRepository
import com.guilherme.knowyourfan.knowyourfan.presentation.AuthenticationViewModel
import com.guilherme.knowyourfan.knowyourfan.presentation.HomeViewModel
import com.guilherme.knowyourfan.knowyourfan.presentation.SignUpViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

expect fun platformModule(activity: Any): Module

val sharedModules = module {
    single<GeminiService> { GeminiImpl() }
    single<RecommendationRepository> { RecommendationImpl(get()) }
    single<DataStoreRepository> { DataStoreImpl(get()) }
    viewModel { AuthenticationViewModel(get()) }
    viewModel { SignUpViewModel(get(), get()) }
    viewModel { HomeViewModel(get(), get(), get(), get(), get()) }
}