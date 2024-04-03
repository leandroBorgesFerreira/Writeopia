package io.writeopia.sqldelight.database.driver

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.worker.WebWorkerDriver
import io.writeopia.sql.WriteopiaDb
import org.w3c.dom.Worker

actual class DriverFactory {
    actual suspend fun createDriver(url: String): SqlDriver =
        WebWorkerDriver(
            Worker(
                js("""new URL("@cashapp/sqldelight-sqljs-worker/sqljs.worker.js", import.meta.url)""")
            )
        ).also {
            WriteopiaDb.Schema.create(it).await()
        }
    }
