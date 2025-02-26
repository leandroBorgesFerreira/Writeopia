package io.writeopia.notemenu.di

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import io.writeopia.repository.UiConfigurationMemoryRepository
import io.writeopia.repository.UiConfigurationRepository
import io.writeopia.viewmodel.UiConfigurationKmpViewModel
import io.writeopia.viewmodel.UiConfigurationViewModel

actual class UiConfigurationInjector {
    actual fun provideUiConfigurationRepository(): UiConfigurationRepository =
        getUiConfigurationMemoryRepository()

    @Composable
    fun provideUiConfigurationViewModel(
        uiConfigurationSqlDelightRepository: UiConfigurationRepository = provideUiConfigurationRepository(),
    ): UiConfigurationViewModel = viewModel {
        UiConfigurationKmpViewModel(uiConfigurationSqlDelightRepository)
    }

    actual companion object {
        private var instanceConfigRepository: UiConfigurationMemoryRepository? = null
        private var instance: UiConfigurationInjector? = null

        fun getUiConfigurationMemoryRepository(): UiConfigurationMemoryRepository =
            instanceConfigRepository ?: kotlin.run {
                UiConfigurationMemoryRepository().also {
                    instanceConfigRepository = it
                }
                instanceConfigRepository!!
            }

        actual fun singleton(): UiConfigurationInjector =
            instance ?: UiConfigurationInjector().also {
                instance = it
            }
    }
}
