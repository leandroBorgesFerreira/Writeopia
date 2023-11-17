package io.writeopia.sdk.persistence.sqldelight

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import io.writeopia.sdk.sql.WriteopiaDb

actual class DriverFactory {
    actual fun createDriver(): SqlDriver {
        val driver: SqlDriver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        WriteopiaDb.Schema.create(driver)
        return driver
    }
}