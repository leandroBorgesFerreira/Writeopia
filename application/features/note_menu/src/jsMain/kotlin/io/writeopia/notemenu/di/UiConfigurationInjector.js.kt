package io.writeopia.notemenu.di

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import io.writeopia.repository.UiConfigurationMemoryRepository
import io.writeopia.repository.UiConfigurationRepository
import io.writeopia.viewmodel.UiConfigurationKmpViewModel
import io.writeopia.viewmodel.UiConfigurationViewModel
import kotlinx.coroutines.CoroutineScope

actual class UiConfigurationInjector {
    actual fun provideUiConfigurationRepository(): UiConfigurationRepository =
        getUiConfigurationMemoryRepository()

    @Composable
    fun provideUiConfigurationViewModel(
        uiConfigurationSqlDelightRepository: UiConfigurationRepository = provideUiConfigurationRepository(),
    ): UiConfigurationViewModel = viewModel {
        UiConfigurationKmpViewModel(uiConfigurationSqlDelightRepository)
    }

    companion object {
        private var instance: UiConfigurationMemoryRepository? = null

        fun getUiConfigurationMemoryRepository(): UiConfigurationMemoryRepository =
            instance ?: kotlin.run {
                instance = UiConfigurationMemoryRepository()
                instance!!
            }
    }
}
