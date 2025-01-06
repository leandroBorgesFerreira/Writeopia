package io.writeopia.notemenu.di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import io.writeopia.repository.UiConfigurationRepository
import io.writeopia.repository.UiConfigurationSqlDelightRepository
import io.writeopia.sql.WriteopiaDb
import io.writeopia.sqldelight.theme.UiConfigurationSqlDelightDao
import io.writeopia.viewmodel.UiConfigurationKmpViewModel
import io.writeopia.viewmodel.UiConfigurationViewModel
import kotlinx.coroutines.CoroutineScope

actual class UiConfigurationInjector(private val writeopiaDb: WriteopiaDb?) {

    private fun provideUiConfigurationSqlDelightDao(): UiConfigurationSqlDelightDao =
        UiConfigurationSqlDelightDao(writeopiaDb)

    actual fun provideUiConfigurationRepository(): UiConfigurationRepository =
        UiConfigurationSqlDelightRepository.singleton(provideUiConfigurationSqlDelightDao())

    @Composable
    fun provideUiConfigurationViewModel(
        uiConfigurationSqlDelightRepository: UiConfigurationRepository =
            provideUiConfigurationRepository(),
    ): UiConfigurationViewModel = viewModel {
        UiConfigurationKmpViewModel(uiConfigurationSqlDelightRepository)
    }
}
