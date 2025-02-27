package io.writeopia.notemenu.di

import androidx.compose.runtime.Composable
import io.writeopia.viewmodel.UiConfigurationViewModel

expect class UiConfigurationInjector {

    @Composable
    fun provideUiConfigurationViewModel(): UiConfigurationViewModel

    companion object {
        fun singleton(): UiConfigurationInjector
    }
}
