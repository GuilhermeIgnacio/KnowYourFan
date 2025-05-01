package com.guilherme.knowyourfan.knowyourfan.data.local

import app.cash.sqldelight.db.SqlDriver

expect class DriverFactory {
    fun createDriver(): SqlDriver
}
