package com.guilherme.knowyourfan.di

import com.guilherme.knowyourfan.knowyourfan.data.remote.api.OpenAiService
import com.guilherme.knowyourfan.knowyourfan.data.remote.api.openai.OpenAiImpl
import com.guilherme.knowyourfan.knowyourfan.presentation.AuthenticationViewModel
import com.guilherme.knowyourfan.knowyourfan.presentation.SignUpViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

expect fun platformModule(activity: Any): Module

val sharedModules = module {
    single<OpenAiService> { OpenAiImpl() }
    viewModel { AuthenticationViewModel(get()) }
    viewModel { SignUpViewModel(get(), get(), get()) }
}