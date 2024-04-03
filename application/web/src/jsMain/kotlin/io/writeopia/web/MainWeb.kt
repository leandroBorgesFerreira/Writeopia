package io.writeopia.web

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import io.writeopia.note_menu.di.NotesConfigurationInjector
import io.writeopia.notes.desktop.components.App
import io.writeopia.sqldelight.database.DatabaseCreation
import io.writeopia.sqldelight.database.DatabaseFactory
import io.writeopia.sqldelight.database.driver.DriverFactory
import io.writeopia.sqldelight.di.SqlDelightDaoInjector
import io.writeopia.ui.drawer.factory.DefaultDrawersJs
import org.jetbrains.skiko.wasm.onWasmReady

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    onWasmReady {
        CanvasBasedWindow("Writeopia") {
            val databaseStateFlow = DatabaseFactory.createDatabase(
                DriverFactory(),
                url = "",
                rememberCoroutineScope()
            )

            Box {
                when (val databaseState = databaseStateFlow.collectAsState().value) {
                    is DatabaseCreation.Complete -> {
                        val database = databaseState.writeopiaDb
                        App(
                            notesConfigurationInjector = NotesConfigurationInjector(database),
                            repositoryInjection = SqlDelightDaoInjector(database),
                            drawersFactory = DefaultDrawersJs
                        )
                    }

                    DatabaseCreation.Loading -> {
                        Text("Loading")
                    }
                }
            }
        }
    }
}

