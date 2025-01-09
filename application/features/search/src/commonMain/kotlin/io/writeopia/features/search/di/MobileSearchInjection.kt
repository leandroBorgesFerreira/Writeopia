package io.writeopia.features.search.di

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import io.writeopia.common.utils.persistence.di.AppDaosInjection
import io.writeopia.features.search.ui.SearchKmpViewModel
import io.writeopia.features.search.ui.SearchViewModel
import io.writeopia.notemenu.data.repository.RoomFolderRepository
import io.writeopia.sdk.persistence.core.di.RepositoryInjector

class MobileSearchInjection(
    private val searchInjection: KmpSearchInjection,
    private val appRoomDaosInjection: AppDaosInjection,
    private val roomInjector: RepositoryInjector,
) : SearchInjection {

    @Composable
    override fun provideViewModelMobile(): SearchViewModel =
        viewModel {
            SearchKmpViewModel(
                searchInjection.provideRepository(
                    folderDao = RoomFolderRepository(appRoomDaosInjection.provideFolderDao()),
                    documentDao = roomInjector.provideDocumentRepository()
                )
            )
        }

    @Composable
    override fun provideViewModel(): SearchViewModel {
        TODO("Not yet implemented")
    }
}
