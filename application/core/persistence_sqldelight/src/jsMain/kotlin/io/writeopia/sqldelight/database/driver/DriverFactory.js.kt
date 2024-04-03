package io.writeopia.sqldelight.database.driver

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.worker.WebWorkerDriver
import org.w3c.dom.Worker

actual class DriverFactory {
    actual fun createDriver(url: String): SqlDriver =
        throw IllegalStateException()
//        WebWorkerDriver(
//            Worker(
//                js("""new URL("@cashapp/sqldelight-sqljs-worker/sqljs.worker.js", import.meta.url)""")
//            )
//        )
    }
