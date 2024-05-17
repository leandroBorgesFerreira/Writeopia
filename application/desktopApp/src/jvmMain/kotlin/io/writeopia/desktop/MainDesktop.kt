package io.writeopia.desktop

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.awtEventOrNull
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isMetaPressed
import androidx.compose.ui.input.key.type
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import io.writeopia.note_menu.di.NotesConfigurationInjector
import io.writeopia.note_menu.di.UiConfigurationInjector
import io.writeopia.notes.desktop.components.App
import io.writeopia.sqldelight.database.DatabaseCreation
import io.writeopia.sqldelight.database.DatabaseFactory
import io.writeopia.sqldelight.database.driver.DriverFactory
import io.writeopia.sqldelight.di.SqlDelightDaoInjector
import kotlinx.coroutines.flow.MutableStateFlow
import java.awt.event.KeyEvent
import androidx.compose.ui.input.key.KeyEvent as AndroidKeyEvent

fun main() = application {
    val coroutineScope = rememberCoroutineScope()
    val databaseStateFlow = DatabaseFactory.createDatabaseAsState(
        DriverFactory(),
        url = "jdbc:sqlite:writeopia.db_1",
        coroutineScope = coroutineScope
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

                val uiConfigurationViewModel = UiConfigurationInjector(database)
                    .provideUiConfigurationViewModel(coroutineScope = coroutineScope)

                val colorTheme =
                    uiConfigurationViewModel.listenForColorTheme { "user_offline" }
                        .collectAsState()
                        .value

                if (colorTheme != null) {
                    App(
                        notesConfigurationInjector = NotesConfigurationInjector(database),
                        repositoryInjection = SqlDelightDaoInjector(database),
                        uiConfigurationInjector = UiConfigurationInjector(database),
                        selectionState = selectionState,
                        isUndoKeyEvent = ::isUndoKeyboardEvent,
                        colorThemeOption = colorTheme,
                        selectColorTheme = uiConfigurationViewModel::changeColorTheme
                    )
                } else {
                    ScreenLoading()
                }

            }

            DatabaseCreation.Loading -> {
                ScreenLoading()
            }
        }
    }
}

@Composable
private fun ScreenLoading() {
    Box(modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
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
