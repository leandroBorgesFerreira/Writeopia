package io.writeopia.database.connection

import com.zaxxer.hikari.HikariDataSource
import java.sql.Connection
import java.sql.DriverManager
import javax.sql.DataSource


object HikariCp {

    fun dataSource(userName: String): DataSource {
//        val conn: Connection? = DriverManager.getConnection(
//            "jdbc:postgresql://localhost:5432/writeopia-sql-database",
//        )
//        if (conn != null) {
//            println("Connection successful!")
//            conn.close()
//        } else {
//            println("Connection failed!")
//            // Check exception message for details
//        }

        return HikariDataSource().apply {
            jdbcUrl = "jdbc:postgresql://localhost:5432/writeopia-sql-database"
//            username = userName
        }
    }
}