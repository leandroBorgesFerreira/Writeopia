package io.writeopia.sqldelight.database.driver

import app.cash.sqldelight.db.SqlDriver

expect class DriverFactory {
    suspend fun createDriver(url: String): SqlDriver
}