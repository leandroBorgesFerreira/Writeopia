package io.writeopia.notemenu.di

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import io.writeopia.persistence.room.WriteopiaApplicationDatabase
import io.writeopia.persistence.room.data.daos.UiConfigurationCommonDao
import io.writeopia.repository.UiConfigurationRepository
import io.writeopia.repository.UiConfigurationRepositoryImpl
import io.writeopia.viewmodel.UiConfigurationKmpViewModel
import io.writeopia.viewmodel.UiConfigurationViewModel

actual class UiConfigurationInjector(
    private val writeopiaApplicationDatabase: WriteopiaApplicationDatabase
) {

    actual fun provideUiConfigurationRepository(): UiConfigurationRepository =
        UiConfigurationRepositoryImpl(
            UiConfigurationCommonDao(writeopiaApplicationDatabase.uiConfigDao())
        )

    @Composable
    fun provideUiConfigurationViewModel(
        uiConfigurationSqlDelightRepository: UiConfigurationRepository =
            provideUiConfigurationRepository(),
    ): UiConfigurationViewModel = viewModel {
        UiConfigurationKmpViewModel(uiConfigurationSqlDelightRepository)
    }
}
