package io.writeopia.sdk.persistence.sqldelight

import app.cash.sqldelight.db.SqlDriver


expect class DriverFactory {
    fun createDriver(): SqlDriver
}
