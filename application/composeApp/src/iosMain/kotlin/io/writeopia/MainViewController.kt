package io.writeopia

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.window.ComposeUIViewController
import androidx.lifecycle.viewmodel.compose.viewModel
import io.writeopia.navigation.MobileNavigationViewModel
import io.writeopia.notemenu.di.NotesInjector
import io.writeopia.notemenu.di.UiConfigurationInjector
import io.writeopia.sqldelight.database.DatabaseCreation
import io.writeopia.sqldelight.database.DatabaseFactory
import io.writeopia.sqldelight.database.driver.DriverFactory
import io.writeopia.sqldelight.di.SqlDelightDaoInjector

fun MainViewController() = ComposeUIViewController {
    val coroutine = rememberCoroutineScope()

    val databaseStateFlow = DatabaseFactory.createDatabaseAsState(
        DriverFactory(),
        url = "",
        coroutineScope = coroutine
    )

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

            val navigationViewModel = viewModel { MobileNavigationViewModel() }
        }

        DatabaseCreation.Loading -> TODO()
    }

    App()
}
