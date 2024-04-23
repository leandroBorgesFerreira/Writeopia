package io.writeopia.menu

import androidx.compose.ui.test.junit4.ComposeContentTestRule
import io.writeopia.note_menu.di.NotesConfigurationInjector
import io.writeopia.notes.desktop.components.App
import io.writeopia.sql.WriteopiaDb
import io.writeopia.sqldelight.database.DatabaseFactory
import io.writeopia.sqldelight.database.driver.DriverFactory
import io.writeopia.sqldelight.di.SqlDelightDaoInjector
import io.writeopia.ui.drawer.factory.DefaultDrawersDesktop

suspend fun startApp(
    composeTestRule: ComposeContentTestRule,
    databaseConfig: suspend (WriteopiaDb) -> Unit = {}
) {
    val database: WriteopiaDb = DatabaseFactory.createDatabase(DriverFactory())
    databaseConfig(database)

    composeTestRule.setContent {
        App(
            notesConfigurationInjector = NotesConfigurationInjector(database),
            repositoryInjection = SqlDelightDaoInjector(database),
            drawersFactory = DefaultDrawersDesktop,
            disableWebsocket = true
        )
    }
}