package com.guilherme.knowyourfan

import android.app.Application
import com.guilherme.knowyourfan.di.initKoin
import org.koin.android.ext.koin.androidContext

class KnowYourFanApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        initKoin{
            androidContext(this@KnowYourFanApplication)
        }

    }
}