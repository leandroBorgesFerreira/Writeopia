package io.writeopia.desktop

import androidx.compose.material.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.awtEventOrNull
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isAltPressed
import androidx.compose.ui.input.key.isCtrlPressed
import androidx.compose.ui.input.key.isMetaPressed
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
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
import java.awt.event.KeyEvent

fun main() = application {
    val databaseStateFlow = DatabaseFactory.createDatabaseAsState(
        DriverFactory(),
//        url = "jdbc:sqlite:", //In Memory
        url = "jdbc:sqlite:writeopia.db",
        rememberCoroutineScope()
    )

    val injector = SqlDelightDaoInjector(null)

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
                    repositoryInjection = injector,
                    DefaultDrawersDesktop,
                    modifier = { writeopiaStateManager ->
                        Modifier.onPreviewKeyEvent { keyEvent ->
                            val shouldHandle = keyEvent.isMetaPressed &&
                                    keyEvent.awtEventOrNull?.keyCode == KeyEvent.VK_Z &&
                                    keyEvent.type == KeyEventType.KeyDown

                            if (shouldHandle) {
                                writeopiaStateManager.undo()
                                true
                            } else {
                                false
                            }
                        }
                    }
                )
            }

            DatabaseCreation.Loading -> {
                Text("Loading")
            }
        }
    }
}
