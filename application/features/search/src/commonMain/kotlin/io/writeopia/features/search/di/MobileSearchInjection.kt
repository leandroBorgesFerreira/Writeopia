package io.writeopia.features.search.di

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import io.writeopia.features.search.ui.SearchKmpViewModel
import io.writeopia.features.search.ui.SearchViewModel
import io.writeopia.notemenu.data.repository.RoomFolderRepository
import io.writeopia.persistence.room.injection.AppRoomDaosInjection
import io.writeopia.persistence.room.injection.RoomRepositoryInjection

class MobileSearchInjection(
    private val searchInjection: KmpSearchInjection,
    private val appRoomDaosInjection: AppRoomDaosInjection,
    private val roomInjector: RoomRepositoryInjection,
) : SearchInjection {

    @Composable
    override fun provideViewModel(): SearchViewModel =
        viewModel {
            SearchKmpViewModel(
                searchInjection.provideRepository(
                    folderDao = RoomFolderRepository(appRoomDaosInjection.provideFolderDao()),
                    documentDao = roomInjector.provideDocumentRepository()
                )
            )
        }
}
