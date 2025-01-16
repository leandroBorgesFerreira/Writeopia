package io.writeopia

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.window.ComposeUIViewController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import io.writeopia.account.di.AccountMenuKmpInjector
import io.writeopia.auth.core.di.KmpAuthCoreInjection
import io.writeopia.auth.core.token.MockTokenHandler
import io.writeopia.auth.di.AuthInjection
import io.writeopia.auth.navigation.authNavigation
import io.writeopia.editor.di.EditorKmpInjector
import io.writeopia.features.search.di.KmpSearchInjection
import io.writeopia.mobile.AppMobile
import io.writeopia.navigation.MobileNavigationViewModel
import io.writeopia.notemenu.data.model.NotesNavigation
import io.writeopia.notemenu.di.NotesInjector
import io.writeopia.notemenu.di.NotesMenuKmpInjection
import io.writeopia.notemenu.di.UiConfigurationInjector
import io.writeopia.notemenu.navigation.navigateToNotes
import io.writeopia.notes.desktop.components.startDestination
import io.writeopia.sdk.network.injector.ConnectionInjector
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

            val authCoreInjection = remember { KmpAuthCoreInjection() }
            val uiConfigurationInjector = remember { UiConfigurationInjector(database) }
            val sqlDelightDaoInjector = remember { SqlDelightDaoInjector(database) }
            val searchInjection = remember { KmpSearchInjection(database) }
            val notesInjector = remember { NotesInjector(database) }

            val uiConfigurationViewModel = uiConfigurationInjector
                .provideUiConfigurationViewModel()

            val notesMenuInjection = remember {
                NotesMenuKmpInjection.mobile(
                    notesInjector,
                    authCoreInjection,
                    sqlDelightDaoInjector,
                )
            }

            val connectionInjection =
                remember {
                    ConnectionInjector(
                        bearerTokenHandler = MockTokenHandler,
                        baseUrl = "https://writeopia.io/api",
                        disableWebsocket = true
                    )
                }

            val editorInjector = EditorKmpInjector.mobile(
                authCoreInjection,
                sqlDelightDaoInjector,
                connectionInjection,
                uiConfigurationInjector.provideUiConfigurationRepository()
            )

            val authInjection = AuthInjection(
                authCoreInjection,
                connectionInjection,
                sqlDelightDaoInjector
            )
            val accountMenuInjector = AccountMenuKmpInjector(authCoreInjection)
            val navigationViewModel = viewModel { MobileNavigationViewModel() }

            val navController = rememberNavController()

            AppMobile(
                navController = navController,
                searchInjector = searchInjection,
                uiConfigViewModel = uiConfigurationViewModel,
                notesMenuInjection = notesMenuInjection,
                editorInjector = editorInjector,
                accountMenuInjector = accountMenuInjector,
                navigationViewModel = navigationViewModel,
            ) {
                authNavigation(navController, authInjection) {
                    navController.navigateToNotes(NotesNavigation.Root)
                }
            }
        }

        DatabaseCreation.Loading -> {
            CircularProgressIndicator()
        }
    }
}
