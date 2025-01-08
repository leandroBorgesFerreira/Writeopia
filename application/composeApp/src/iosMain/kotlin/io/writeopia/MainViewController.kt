package io.writeopia

import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import androidx.lifecycle.viewmodel.compose.viewModel
import io.writeopia.account.di.AccountMenuKmpInjector
import io.writeopia.auth.core.di.KmpAuthCoreInjection
import io.writeopia.auth.core.token.MockTokenHandler
import io.writeopia.editor.di.EditorKmpInjector
import io.writeopia.features.search.di.KmpSearchInjection
import io.writeopia.features.search.di.MobileSearchInjection
import io.writeopia.navigation.MobileNavigationViewModel
import io.writeopia.notemenu.di.NotesInjector
import io.writeopia.notemenu.di.NotesMenuKmpInjection
import io.writeopia.persistence.room.DatabaseConfigIos
import io.writeopia.persistence.room.WriteopiaApplicationDatabase
import io.writeopia.persistence.room.injection.AppRoomDaosInjection
import io.writeopia.persistence.room.injection.RoomRepositoryInjection
import io.writeopia.sdk.network.injector.ConnectionInjector

fun MainViewController() = ComposeUIViewController {
    val database = WriteopiaApplicationDatabase.database(DatabaseConfigIos.roomBuilder())

    val authCoreInjection = remember { KmpAuthCoreInjection() }
    val appDaosInjection = AppRoomDaosInjection(database)
    val notesInjector = NotesInjector(appDaosInjection)
    val repositoryInjection = RoomRepositoryInjection(database)
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
        repositoryInjection,
        connectionInjection,
        uiConfigInjection.provideUiConfigurationRepository()
    )
    val accountMenuInjector = AccountMenuKmpInjector(authCoreInjection)
    val notesMenuInjection = NotesMenuKmpInjection.mobile(
        notesInjector,
        authCoreInjection,
        repositoryInjection,
    )

    val searchInjector = remember {
        MobileSearchInjection(
            searchInjection = KmpSearchInjection(),
            appRoomDaosInjection = appDaosInjection,
            roomInjector = repositoryInjection
        )
    }

    val navigationViewModel = viewModel { MobileNavigationViewModel() }


//    AppMobile()
}
