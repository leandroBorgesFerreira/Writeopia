package io.writeopia.desktop

import androidx.compose.material.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.awt.awtEventOrNull
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isMetaPressed
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
import io.writeopia.theme.WrieopiaTheme
import io.writeopia.ui.drawer.factory.DefaultDrawersDesktop
import kotlinx.coroutines.flow.MutableStateFlow
import java.awt.event.KeyEvent
import androidx.compose.ui.input.key.KeyEvent as AndroidKeyEvent

fun main() = application {
    val databaseStateFlow = DatabaseFactory.createDatabaseAsState(
        DriverFactory(),
        url = "jdbc:sqlite:writeopia.db_1",
        rememberCoroutineScope()
    )

    val selectionState = MutableStateFlow(false)

    Window(
        onCloseRequest = ::exitApplication,
        title = "Writeopia",
        state = rememberWindowState(width = 1100.dp, height = 800.dp),
        onPreviewKeyEvent = { keyEvent ->
            if (isSelectionKeyEventStart(keyEvent)) {
                selectionState.value = true
            } else if (isSelectionKeyEventStop(keyEvent)) {
                selectionState.value = false
            }

            false
        }
    ) {
            when (val databaseState = databaseStateFlow.collectAsState().value) {
                is DatabaseCreation.Complete -> {
                    val database = databaseState.writeopiaDb

                    App(
                        notesConfigurationInjector = NotesConfigurationInjector(database),
                        repositoryInjection = SqlDelightDaoInjector(database),
                        selectionState = selectionState,
                        isUndoKeyEvent = ::isUndoKeyboardEvent
                    )
                }

                DatabaseCreation.Loading -> {
                    Text("Loading")
                }
            }
        }
}

private fun isUndoKeyboardEvent(keyEvent: AndroidKeyEvent) =
    keyEvent.isMetaPressed &&
        keyEvent.awtEventOrNull?.keyCode == KeyEvent.VK_Z &&
        keyEvent.type == KeyEventType.KeyDown

private fun isSelectionKeyEventStart(keyEvent: AndroidKeyEvent) =
    keyEvent.awtEventOrNull?.keyCode == KeyEvent.VK_META &&
        keyEvent.type == KeyEventType.KeyDown

private fun isSelectionKeyEventStop(keyEvent: AndroidKeyEvent) =
    keyEvent.awtEventOrNull?.keyCode == KeyEvent.VK_META &&
        keyEvent.type == KeyEventType.KeyUp
