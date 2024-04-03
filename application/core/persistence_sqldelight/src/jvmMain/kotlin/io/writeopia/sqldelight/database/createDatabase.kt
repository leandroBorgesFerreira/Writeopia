package io.writeopia.sqldelight.database

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import io.writeopia.sql.WriteopiaDb
import io.writeopia.sqldelight.database.driver.DriverFactory

fun createDatabase(
    driverFactory: DriverFactory,
    url: String = JdbcSqliteDriver.IN_MEMORY
): WriteopiaDb {
    val driver = driverFactory.createDriver(url)
    return WriteopiaDb(driver)
}

