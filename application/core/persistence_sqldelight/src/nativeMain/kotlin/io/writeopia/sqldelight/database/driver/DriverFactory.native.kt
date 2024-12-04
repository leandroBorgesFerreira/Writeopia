package io.writeopia.sqldelight.database.driver

import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import io.writeopia.sql.WriteopiaDb

actual class DriverFactory {
    actual suspend fun createDriver(
        url: String,
    ): SqlDriver = NativeSqliteDriver(WriteopiaDb.Schema.synchronous(), "writeopia.db")
}
