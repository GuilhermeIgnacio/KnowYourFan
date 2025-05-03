package com.guilherme.knowyourfan

import android.app.Activity
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.guilherme.knowyourfan.app.App
import com.guilherme.knowyourfan.di.initKoin
import com.guilherme.knowyourfan.knowyourfan.presentation.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()

        initKoin(config = null, activity = this@MainActivity)
        setContent {

            val context = LocalContext.current
            (context as Activity)?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

                AppTheme {
                    App()
                }
            }

    }
}