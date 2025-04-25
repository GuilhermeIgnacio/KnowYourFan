package com.guilherme.knowyourfan

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform