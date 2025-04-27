package com.guilherme.knowyourfan.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(config: KoinAppDeclaration? = null, activity: Any) {
    startKoin {
        config?.invoke(this)
        modules(sharedModules, platformModule(activity = activity))
    }
}