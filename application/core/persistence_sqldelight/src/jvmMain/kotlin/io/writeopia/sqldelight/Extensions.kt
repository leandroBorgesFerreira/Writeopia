package io.writeopia.sqldelight

import io.writeopia.sdk.persistence.core.di.DaosInjector
import io.writeopia.sqldelight.database.driver.DriverFactory
import io.writeopia.sqldelight.di.SqlDelightDaoInjector

fun DaosInjector.Companion.create(): DaosInjector = SqlDelightDaoInjector(DriverFactory())