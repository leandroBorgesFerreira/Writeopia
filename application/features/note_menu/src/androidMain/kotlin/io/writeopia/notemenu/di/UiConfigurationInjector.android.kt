package io.writeopia.notemenu.di

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import io.writeopia.core.configuration.di.UiConfigurationCoreInjector
import io.writeopia.viewmodel.UiConfigurationKmpViewModel
import io.writeopia.viewmodel.UiConfigurationViewModel

actual class UiConfigurationInjector private constructor() {

    @Composable
    actual fun provideUiConfigurationViewModel(): UiConfigurationViewModel = viewModel {
        UiConfigurationKmpViewModel(
            UiConfigurationCoreInjector.singleton().provideUiConfigurationRepository()
        )
    }

    actual companion object {
        private var instance: UiConfigurationInjector? = null

        actual fun singleton(): UiConfigurationInjector =
            instance ?: UiConfigurationInjector().also {
                instance = it
            }
    }
}
