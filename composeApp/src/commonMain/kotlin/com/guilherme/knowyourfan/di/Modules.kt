package com.guilherme.knowyourfan.di

import com.guilherme.knowyourfan.knowyourfan.presentation.AuthenticationViewModel
import com.guilherme.knowyourfan.knowyourfan.presentation.SignUpViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

expect val platformModule: Module

val sharedModules = module {
    viewModel { AuthenticationViewModel(get()) }
    viewModel { SignUpViewModel(get()) }
}