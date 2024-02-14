package io.writeopia.database.connection

import com.zaxxer.hikari.HikariDataSource
import javax.sql.DataSource

object HikariCp {

    fun dataSource(userName: String) : DataSource =
        HikariDataSource().apply {
            jdbcUrl = "jdbc:postgresql://localhost:50000/writeopia-sql-database"
            username = userName
        }
}