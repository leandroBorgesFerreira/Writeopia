package io.writeopia.notes.desktop

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import io.writeopia.notes.desktop.components.App
import io.writeopia.sqldelight.database.createDatabase
import io.writeopia.sqldelight.database.driver.DriverFactory

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Writeopia for Desktop",
        state = rememberWindowState(width = 1100.dp, height = 800.dp)
    ) {
        val database = createDatabase(DriverFactory(), url = "jdbc:sqlite:writeopia.db")
        App(database)
    }
}



