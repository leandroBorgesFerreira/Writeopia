package io.writeopia.sqldelight

import io.writeopia.sqldelight.database.driver.DriverFactory
import io.writeopia.sqldelight.di.SqlDelightDaoInjector

fun SqlDelightDaoInjector.Companion.create() = SqlDelightDaoInjector(DriverFactory())