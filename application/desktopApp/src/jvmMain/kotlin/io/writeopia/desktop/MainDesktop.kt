package io.writeopia.desktop

import androidx.compose.material.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import io.writeopia.note_menu.di.NotesConfigurationInjector
import io.writeopia.notes.desktop.components.App
import io.writeopia.sqldelight.database.DatabaseCreation
import io.writeopia.sqldelight.database.DatabaseFactory
import io.writeopia.sqldelight.database.driver.DriverFactory
import io.writeopia.sqldelight.di.SqlDelightDaoInjector
import io.writeopia.ui.drawer.factory.DefaultDrawersDesktop

fun main() = application {
    val databaseStateFlow = DatabaseFactory.createDatabase(
        DriverFactory(),
//        url = "jdbc:sqlite:", //In Memory
        url = "jdbc:sqlite:writeopia.db",
        rememberCoroutineScope()
    )

    Window(
        onCloseRequest = ::exitApplication,
        title = "Writeopia for Desktop",
        state = rememberWindowState(width = 1100.dp, height = 800.dp)
    ) {
        when (val databaseState = databaseStateFlow.collectAsState().value) {
            is DatabaseCreation.Complete -> {
                val database = databaseState.writeopiaDb
                App(
                    notesConfigurationInjector = NotesConfigurationInjector(database),
                    repositoryInjection = SqlDelightDaoInjector(database),
                    DefaultDrawersDesktop
                )
            }

            DatabaseCreation.Loading -> {
                Text("Loading")
            }
        }
    }
}

