package com.guilherme.knowyourfan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.guilherme.knowyourfan.app.App
import com.guilherme.knowyourfan.knowyourfan.presentation.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppTheme {
                App()
            }
        }
    }
}