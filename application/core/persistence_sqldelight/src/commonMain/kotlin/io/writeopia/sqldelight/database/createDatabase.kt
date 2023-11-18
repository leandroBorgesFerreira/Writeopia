package io.writeopia.sqldelight.database

import io.writeopia.sql.WriteopiaDb
import io.writeopia.sqldelight.database.driver.DriverFactory

fun createDatabase(driverFactory: DriverFactory): WriteopiaDb {
    val driver = driverFactory.createDriver()
    return WriteopiaDb(driver)
}

