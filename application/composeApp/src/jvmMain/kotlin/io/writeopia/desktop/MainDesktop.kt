package io.writeopia.desktop

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.awtEventOrNull
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isMetaPressed
import androidx.compose.ui.input.key.isShiftPressed
import androidx.compose.ui.input.key.type
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import io.writeopia.notemenu.di.NotesInjector
import io.writeopia.notemenu.di.UiConfigurationInjector
import io.writeopia.notes.desktop.components.DesktopApp
import io.writeopia.sqldelight.database.DatabaseCreation
import io.writeopia.sqldelight.database.DatabaseFactory
import io.writeopia.sqldelight.database.driver.DriverFactory
import io.writeopia.sqldelight.di.SqlDelightDaoInjector
import io.writeopia.ui.image.ImageLoadConfig
import io.writeopia.ui.keyboard.KeyboardEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import java.awt.event.KeyEvent
import java.io.File
import androidx.compose.ui.input.key.KeyEvent as AndroidKeyEvent

private const val APP_DIRECTORY = ".writeopia"
private const val DB_VERSION = 15

fun main() = application {
    DesktopApp()
}

@Composable
private fun ApplicationScope.DesktopApp(onCloseRequest: () -> Unit = ::exitApplication) {
    ImageLoadConfig.configImageLoad()

    val coroutineScope = rememberCoroutineScope()

    val homeDirectory: String = System.getProperty("user.home")
    val appDirectory = File(homeDirectory, APP_DIRECTORY)

    val databaseStateFlow = DatabaseFactory.createDatabaseAsState(
        DriverFactory(),
        url = "jdbc:sqlite:${appDirectory.path}:writeopia.db_$DB_VERSION",
        coroutineScope = coroutineScope
    )

    val selectionState = MutableStateFlow(false)
    val keyboardEventFlow = MutableStateFlow<KeyboardEvent?>(null)
    val sendEvent = { keyboardEvent: KeyboardEvent ->
        coroutineScope.launch(Dispatchers.IO) {
            keyboardEventFlow.tryEmit(keyboardEvent)
            delay(20)
            keyboardEventFlow.tryEmit(KeyboardEvent.IDLE)
        }
    }

    Window(
        onCloseRequest = onCloseRequest,
        title = "Writeopia",
        state = rememberWindowState(width = 1100.dp, height = 800.dp),
        onPreviewKeyEvent = { keyEvent ->
            if (isSelectionKeyEventStart(keyEvent)) {
                selectionState.value = true
            } else if (isSelectionKeyEventStop(keyEvent)) {
                selectionState.value = false
            }

            when {
                isSelectionKeyEventStart(keyEvent) -> {
                    selectionState.value = true
                    false
                }

                isSelectionKeyEventStop(keyEvent) -> {
                    selectionState.value = false
                    false
                }

                isDeleteEvent(keyEvent) -> {
                    sendEvent(KeyboardEvent.DELETE)
                    false
                }

                isSelectAllEvent(keyEvent) -> {
                    sendEvent(KeyboardEvent.SELECT_ALL)
                    false
                }

                isBoxEvent(keyEvent) -> {
                    sendEvent(KeyboardEvent.BOX)
                    false
                }

                isBoldEvent(keyEvent) -> {
                    sendEvent(KeyboardEvent.BOLD)
                    false
                }

                isItalicEvent(keyEvent) -> {
                    sendEvent(KeyboardEvent.ITALIC)
                    false
                }

                isUnderlineEvent(keyEvent) -> {
                    sendEvent(KeyboardEvent.UNDERLINE)
                    false
                }

                isLinkEvent(keyEvent) -> {
                    sendEvent(KeyboardEvent.LINK)
                    false
                }

                isLocalSaveEvent(keyEvent) -> {
                    sendEvent(KeyboardEvent.LOCAL_SAVE)
                    false
                }

                else -> false
            }
        }
    ) {
        when (val databaseState = databaseStateFlow.collectAsState().value) {
            is DatabaseCreation.Complete -> {
                val database = databaseState.writeopiaDb

                val uiConfigurationInjector = remember { UiConfigurationInjector(database) }
                val sqlDelightDaoInjector = remember { SqlDelightDaoInjector(database) }
                val notesInjector = remember { NotesInjector(database) }

                val uiConfigurationViewModel = uiConfigurationInjector
                    .provideUiConfigurationViewModel()

                val colorTheme =
                    uiConfigurationViewModel.listenForColorTheme { "disconnected_user" }

                DesktopApp(
                    notesInjector = notesInjector,
                    repositoryInjection = sqlDelightDaoInjector,
                    uiConfigurationInjector = uiConfigurationInjector,
                    selectionState = selectionState,
                    keyboardEventFlow = keyboardEventFlow.filterNotNull(),
                    coroutineScope = coroutineScope,
                    isUndoKeyEvent = ::isUndoKeyboardEvent,
                    colorThemeOption = colorTheme,
                    selectColorTheme = uiConfigurationViewModel::changeColorTheme,
                    writeopiaDb = database
                )
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
