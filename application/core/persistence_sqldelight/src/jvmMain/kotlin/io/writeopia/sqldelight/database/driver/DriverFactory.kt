package io.writeopia.sqldelight.database.driver

import app.cash.sqldelight.async.coroutines.awaitCreate
import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import io.writeopia.sql.WriteopiaDb
import java.util.Properties

actual class DriverFactory {
    actual suspend fun createDriver(
        url: String,
    ): SqlDriver = JdbcSqliteDriver(url, Properties(), WriteopiaDb.Schema.synchronous()).also {
//        WriteopiaDb.Schema.awaitCreate(it)
    }
}
