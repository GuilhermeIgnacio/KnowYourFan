package com.guilherme.knowyourfan.app

import kotlinx.serialization.Serializable

sealed interface Route {

    @Serializable
    data object AuthGraph : Route

    @Serializable
    data object AuthenticationScreen : Route

    @Serializable
    data object SignUpScreen: Route

    @Serializable
    data object HomeGraph : Route

    @Serializable
    data object HomeScreen : Route

}