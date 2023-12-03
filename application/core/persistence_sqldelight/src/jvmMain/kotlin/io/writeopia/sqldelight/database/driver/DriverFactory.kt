package io.writeopia.sqldelight.database.driver

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import io.writeopia.sql.WriteopiaDb
import java.util.Properties

actual class DriverFactory {
    actual fun createDriver(
        url: String,
        properties: Properties,
    ): SqlDriver = JdbcSqliteDriver(url, properties, WriteopiaDb.Schema)
}