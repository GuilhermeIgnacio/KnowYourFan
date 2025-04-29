package com.guilherme.knowyourfan.di

import com.guilherme.knowyourfan.knowyourfan.data.remote.api.gemini.GeminiService
import com.guilherme.knowyourfan.knowyourfan.data.remote.api.gemini.GeminiImpl
import com.guilherme.knowyourfan.knowyourfan.presentation.AuthenticationViewModel
import com.guilherme.knowyourfan.knowyourfan.presentation.SignUpViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

expect fun platformModule(activity: Any): Module

val sharedModules = module {
    single<GeminiService> { GeminiImpl() }
    viewModel { AuthenticationViewModel(get()) }
    viewModel { SignUpViewModel(get(), get()) }
}