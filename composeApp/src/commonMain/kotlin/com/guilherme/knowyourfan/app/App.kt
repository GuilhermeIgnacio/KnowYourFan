package com.guilherme.knowyourfan.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.guilherme.knowyourfan.knowyourfan.presentation.AuthenticationScreen

@Composable
fun App() {

    val navController = rememberNavController()
    Surface(modifier = Modifier.fillMaxSize()) {
        NavHost(navController = navController, startDestination = Route.AuthGraph) {
            navigation<Route.AuthGraph>(
                startDestination = Route.AuthenticationScreen
            ) {
                composable<Route.AuthenticationScreen> {
                    AuthenticationScreen(
                        onAuth = { navController.navigate(Route.HomeGraph) }
                    )
                }

            }

            navigation<Route.HomeGraph>(
                startDestination = Route.HomeScreen
            ) {
                composable<Route.HomeScreen> {  }
            }

        }
    }

}