package io.writeopia.notemenu.di

import io.writeopia.repository.UiConfigurationMemoryRepository
import io.writeopia.repository.UiConfigurationRepository
import io.writeopia.viewmodel.UiConfigurationKmpViewModel
import io.writeopia.viewmodel.UiConfigurationViewModel
import kotlinx.coroutines.CoroutineScope

actual class UiConfigurationInjector {
    actual fun provideUiConfigurationRepository(): UiConfigurationRepository =
        getUiConfigurationMemoryRepository()

    fun provideUiConfigurationViewModel(
        uiConfigurationSqlDelightRepository: UiConfigurationRepository = provideUiConfigurationRepository(),
        coroutineScope: CoroutineScope? = null
    ): UiConfigurationViewModel =
        UiConfigurationKmpViewModel(uiConfigurationSqlDelightRepository).apply {
            if (coroutineScope != null) {
                initCoroutine(coroutineScope = coroutineScope)
            }
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
