package io.writeopia.sqldelight.database

import io.writeopia.sql.WriteopiaDb
import io.writeopia.sqldelight.database.driver.DriverFactory

fun createDatabase(
    driverFactory: DriverFactory,
    url: String
): WriteopiaDb {
    val driver = driverFactory.createDriver(url)
    return WriteopiaDb(driver)
}