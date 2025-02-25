package io.writeopia.sqldelight.database

import io.writeopia.sql.WriteopiaDb
import io.writeopia.sqldelight.database.driver.DriverFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

object DatabaseFactory {
    private var created = false
    private val databaseState = MutableStateFlow<DatabaseCreation>(DatabaseCreation.Loading)

    fun createDatabaseAsState(
        driverFactory: DriverFactory,
        url: String,
        coroutineScope: CoroutineScope
    ): StateFlow<DatabaseCreation> {
        if (!created) {
            created = true
            coroutineScope.launch {
                databaseState.value = DatabaseCreation.Complete(createDatabase(driverFactory, url))
            }
        }

        return databaseState.asStateFlow()
    }

    fun createDatabase(
        driverFactory: DriverFactory,
        url: String = "jdbc:sqlite:"
    ): WriteopiaDb {
        val driver = driverFactory.createDriver(url)
        return WriteopiaDb(driver)
    }

}

sealed interface DatabaseCreation {
    data object Loading : DatabaseCreation
    data class Complete(val writeopiaDb: WriteopiaDb) : DatabaseCreation
}
