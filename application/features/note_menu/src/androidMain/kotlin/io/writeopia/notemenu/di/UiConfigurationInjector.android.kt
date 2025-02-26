package io.writeopia.notemenu.di

import android.content.SharedPreferences
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import io.writeopia.common.utils.di.SharedPreferencesInjector
import io.writeopia.repository.UiConfigurationPreferenceDao
import io.writeopia.repository.UiConfigurationRepository
import io.writeopia.repository.UiConfigurationRepositoryImpl
import io.writeopia.viewmodel.UiConfigurationKmpViewModel
import io.writeopia.viewmodel.UiConfigurationViewModel

actual class UiConfigurationInjector private constructor(
    private val sharedPreferences: SharedPreferences,
) {

    actual fun provideUiConfigurationRepository(): UiConfigurationRepository =
        UiConfigurationRepositoryImpl(UiConfigurationPreferenceDao(sharedPreferences))

    @Composable
    fun provideUiConfigurationViewModel(
        uiConfigurationSqlDelightRepository: UiConfigurationRepository =
            provideUiConfigurationRepository(),
    ): UiConfigurationViewModel = viewModel {
        UiConfigurationKmpViewModel(uiConfigurationSqlDelightRepository)
    }

    actual companion object {
        private var instance: UiConfigurationInjector? = null

        actual fun singleton(): UiConfigurationInjector =
            instance ?: UiConfigurationInjector(
                SharedPreferencesInjector.get().sharedPreferences
            ).also {
                instance = it
            }
    }
}
