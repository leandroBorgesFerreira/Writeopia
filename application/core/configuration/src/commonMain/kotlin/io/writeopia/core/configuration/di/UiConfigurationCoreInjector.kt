package io.writeopia.core.configuration.di

import io.writeopia.repository.UiConfigurationRepository

expect class UiConfigurationCoreInjector {

    fun provideUiConfigurationRepository(): UiConfigurationRepository

    companion object {
        fun singleton(): UiConfigurationCoreInjector
    }
}
