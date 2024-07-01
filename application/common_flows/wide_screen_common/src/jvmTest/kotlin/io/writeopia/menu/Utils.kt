package io.writeopia.menu

import androidx.compose.ui.test.junit4.ComposeContentTestRule
import io.writeopia.model.ColorThemeOption
import io.writeopia.note_menu.di.NotesInjector
import io.writeopia.note_menu.di.UiConfigurationInjector
import io.writeopia.notes.desktop.components.App
import io.writeopia.sql.WriteopiaDb
import io.writeopia.sqldelight.database.DatabaseFactory
import io.writeopia.sqldelight.database.driver.DriverFactory
import io.writeopia.sqldelight.di.SqlDelightDaoInjector
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow

suspend fun startApp(
    composeTestRule: ComposeContentTestRule,
    coroutineScope: CoroutineScope,
    databaseConfig: suspend (WriteopiaDb) -> Unit = {}
) {
    val database: WriteopiaDb = DatabaseFactory.createDatabase(DriverFactory())
    databaseConfig(database)

    composeTestRule.setContent {
        App(
            notesInjector = NotesInjector(database),
            repositoryInjection = SqlDelightDaoInjector(database),
            uiConfigurationInjector = UiConfigurationInjector(database),
            coroutineScope = coroutineScope,
            colorThemeOption = MutableStateFlow(ColorThemeOption.DARK),
            disableWebsocket = true,
            isUndoKeyEvent = { false },
            selectColorTheme = {},
            selectionState = MutableStateFlow(false)
        )
    }
}
