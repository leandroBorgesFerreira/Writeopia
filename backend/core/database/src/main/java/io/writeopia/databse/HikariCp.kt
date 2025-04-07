package io.writeopia.databse

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.asJdbcDriver
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.writeopia.sql.WriteopiaDbBackend

private val config = HikariConfig().apply {
    jdbcUrl = "jdbc:postgresql://localhost:5432/writeopia"
    username = "postgres"
    password = "postgres"
    maximumPoolSize = 10
    isAutoCommit = true
    transactionIsolation = "TRANSACTION_REPEATABLE_READ"
}

private val dataSource = HikariDataSource(config)

private val driver = dataSource.asJdbcDriver()

object HikariCp {
    fun driver(): SqlDriver = driver
}
