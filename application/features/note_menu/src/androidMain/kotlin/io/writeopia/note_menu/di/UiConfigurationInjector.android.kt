package io.writeopia.note_menu.di

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import io.writeopia.note_menu.viewmodel.UiConfigurationAndroidViewModel
import io.writeopia.persistence.room.WriteopiaApplicationDatabase
import io.writeopia.repository.UiConfigurationRepository
import io.writeopia.repository.UiConfigurationRoomRepository
import io.writeopia.viewmodel.UiConfigurationKmpViewModel
import io.writeopia.viewmodel.UiConfigurationViewModel

actual class UiConfigurationInjector(private val database: WriteopiaApplicationDatabase) {

    actual fun provideUiConfigurationRepository(): UiConfigurationRepository =
        UiConfigurationRoomRepository(database.uiConfigurationDao())

    @Composable
    fun provideUiConfigurationViewModel(
        uiConfigurationSqlDelightRepository: UiConfigurationRepository =
            provideUiConfigurationRepository(),
    ): UiConfigurationViewModel = viewModel {
        UiConfigurationAndroidViewModel(
            UiConfigurationKmpViewModel(uiConfigurationSqlDelightRepository)
        )
    }
}
