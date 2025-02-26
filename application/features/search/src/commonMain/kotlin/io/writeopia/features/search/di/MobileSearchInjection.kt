package io.writeopia.features.search.di

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import io.writeopia.common.utils.persistence.di.AppDaosInjection
import io.writeopia.core.folders.repository.RoomFolderRepository
import io.writeopia.features.search.ui.SearchKmpViewModel
import io.writeopia.features.search.ui.SearchViewModel
import io.writeopia.sdk.persistence.core.di.RepositoryInjector

class MobileSearchInjection(
    private val appRoomDaosInjection: AppDaosInjection,
    private val roomInjector: RepositoryInjector,
) : SearchInjection {

    @Composable
    override fun provideViewModel(): SearchViewModel =
        viewModel {
            SearchKmpViewModel(
                KmpSearchInjection.singleton().provideRepository(
                    folderDao = RoomFolderRepository(appRoomDaosInjection.provideFolderDao()),
                    documentDao = roomInjector.provideDocumentRepository()
                )
            )
        }
}
