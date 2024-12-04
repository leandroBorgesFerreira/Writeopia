package io.writeopia.notemenu.di

import io.writeopia.repository.UiConfigurationRepository

expect class UiConfigurationInjector {

    fun provideUiConfigurationRepository(): UiConfigurationRepository
}
