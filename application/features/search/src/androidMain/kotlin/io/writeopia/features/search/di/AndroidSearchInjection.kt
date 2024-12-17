package io.writeopia.features.search.di

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import io.writeopia.features.search.ui.AndroidSearchViewModel
import io.writeopia.features.search.ui.SearchKmpViewModel
import io.writeopia.features.search.ui.SearchViewModel
import io.writeopia.notemenu.data.repository.RoomFolderRepository
import io.writeopia.persistence.room.injection.AppRoomDaosInjection
import io.writeopia.persistence.room.injection.RoomRepositoryInjection
import kotlinx.coroutines.CoroutineScope

class AndroidSearchInjection(
    private val searchInjection: KmpSearchInjection,
    private val appRoomDaosInjection: AppRoomDaosInjection,
    private val roomInjector: RoomRepositoryInjection,
) : SearchInjection {

    @Composable
    override fun provideViewModelMobile(coroutineScope: CoroutineScope?): SearchViewModel {
        val viewModel = SearchKmpViewModel(
            searchInjection.provideRepository(
                folderDao = RoomFolderRepository(appRoomDaosInjection.provideFolderDao()),
                documentDao = roomInjector.provideDocumentRepository()
            )
        )

        return viewModel {
            AndroidSearchViewModel(viewModel)
        }
    }

    override fun provideViewModel(coroutineScope: CoroutineScope?): SearchViewModel {
        throw IllegalStateException("This injection should not be used")
    }
}
