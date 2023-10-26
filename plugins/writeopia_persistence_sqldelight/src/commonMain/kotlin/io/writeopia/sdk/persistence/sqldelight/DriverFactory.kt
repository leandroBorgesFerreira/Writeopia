package io.writeopia.sdk.persistence.sqldelight

import app.cash.sqldelight.db.SqlDriver
import io.writeopia.sql.WriteopiaDb


expect class DriverFactory {
    fun createDriver(): SqlDriver
}

fun createDatabase(driverFactory: DriverFactory): WriteopiaDb {
    val driver = driverFactory.createDriver()

    return WriteopiaDb(driver)
}