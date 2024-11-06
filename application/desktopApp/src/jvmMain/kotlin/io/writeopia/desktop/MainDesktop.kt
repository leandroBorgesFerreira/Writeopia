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
import androidx.compose.ui.input.key.type
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import io.writeopia.note_menu.di.NotesInjector
import io.writeopia.note_menu.di.UiConfigurationInjector
import io.writeopia.notes.desktop.components.App
import io.writeopia.sqldelight.database.DatabaseCreation
import io.writeopia.sqldelight.database.DatabaseFactory
import io.writeopia.sqldelight.database.driver.DriverFactory
import io.writeopia.sqldelight.di.SqlDelightDaoInjector
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
private const val DB_VERSION = 5

fun main() = application {
    DesktopApp()
//    val pageState = MutableStateFlow(0)
//    val slides = Fixture.document().second
//
//    Window(onCloseRequest = ::exitApplication, title = "Writeopia") {
//        WrieopiaTheme(darkTheme = true) {
//            WriteopiaPresentationScreen(
//                modifier = Modifier.fillMaxSize().background(Color(0xFF252525)),
//                currentPage = pageState,
//                moveSlide = { page ->
//                    val lastIndex = slides.lastIndex
//                    val limitTop = if (page >= lastIndex) lastIndex else page
//                    val limit = if (limitTop < 0) 0 else limitTop
//
//                    println("lastIndex: $lastIndex, limitTop: $limitTop, limit: $limit")
//                    pageState.value = limit
//                },
//                data = slides
//            )
//        }
//    }
}

@Composable
private fun ApplicationScope.DesktopApp(onCloseRequest: () -> Unit = ::exitApplication) {
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

                isMoveDownEvent(keyEvent) -> {
                    coroutineScope.launch(Dispatchers.IO) {
                        keyboardEventFlow.tryEmit(KeyboardEvent.MOVE_DOWN)
                        delay(30)
                        keyboardEventFlow.tryEmit(KeyboardEvent.IDLE)
                    }

                    true
                }

                isMoveUpEvent(keyEvent) -> {
                    coroutineScope.launch(Dispatchers.IO) {
                        keyboardEventFlow.tryEmit(KeyboardEvent.MOVE_UP)
                        delay(30)
                        keyboardEventFlow.tryEmit(KeyboardEvent.IDLE)
                    }

                    true
                }

                isDeleteEvent(keyEvent) -> {
                    coroutineScope.launch(Dispatchers.IO) {
                        keyboardEventFlow.tryEmit(KeyboardEvent.DELETE)
                        delay(30)
                        keyboardEventFlow.tryEmit(KeyboardEvent.IDLE)
                    }

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

                val uiConfigurationViewModel = uiConfigurationInjector
                    .provideUiConfigurationViewModel(coroutineScope = coroutineScope)

                val colorTheme =
                    uiConfigurationViewModel.listenForColorTheme { "disconnected_user" }

                App(
                    notesInjector = NotesInjector(database),
                    repositoryInjection = SqlDelightDaoInjector(database),
                    uiConfigurationInjector = uiConfigurationInjector,
                    selectionState = selectionState,
                    keyboardEventFlow = keyboardEventFlow.filterNotNull(),
                    coroutineScope = coroutineScope,
                    isUndoKeyEvent = ::isUndoKeyboardEvent,
                    colorThemeOption = colorTheme,
                    selectColorTheme = uiConfigurationViewModel::changeColorTheme
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

private fun isMoveDownEvent(keyEvent: AndroidKeyEvent) =
    keyEvent.awtEventOrNull?.keyCode == KeyEvent.VK_DOWN

private fun isDeleteEvent(keyEvent: AndroidKeyEvent) =
    keyEvent.awtEventOrNull?.keyCode == KeyEvent.VK_DELETE ||
        keyEvent.awtEventOrNull?.keyCode == KeyEvent.VK_BACK_SPACE

private fun isMoveUpEvent(keyEvent: AndroidKeyEvent) =
    keyEvent.awtEventOrNull?.keyCode == KeyEvent.VK_UP
