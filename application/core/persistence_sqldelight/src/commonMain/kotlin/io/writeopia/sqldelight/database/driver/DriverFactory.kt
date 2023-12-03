package io.writeopia.sqldelight.database.driver

import app.cash.sqldelight.db.SqlDriver
import java.util.Properties

expect class DriverFactory {
    fun createDriver(
        url: String,
        properties: Properties = Properties(),
    ): SqlDriver
}