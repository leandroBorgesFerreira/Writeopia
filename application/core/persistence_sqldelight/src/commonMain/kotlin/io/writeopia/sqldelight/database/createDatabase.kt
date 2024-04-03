package io.writeopia.sqldelight.database

import io.writeopia.sql.WriteopiaDb
import io.writeopia.sqldelight.database.driver.DriverFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


fun createDatabase(
    driverFactory: DriverFactory,
    url: String,
    coroutineScope: CoroutineScope
): StateFlow<DatabaseCreation> {
    println("Criando db")
    val databaseState = MutableStateFlow<DatabaseCreation>(DatabaseCreation.Loading)

    coroutineScope.launch {
        val driver = driverFactory.createDriver(url)
        databaseState.value = DatabaseCreation.Complete(WriteopiaDb(driver))
    }

    return databaseState.asStateFlow()
}

sealed interface DatabaseCreation {
    data object Loading : DatabaseCreation
    data class Complete(val writeopiaDb: WriteopiaDb) : DatabaseCreation
}