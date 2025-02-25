package io.writeopia.notemenu.di

import io.writeopia.repository.UiConfigurationRepository

expect class AndroidUiConfigurationInjector {

    fun provideUiConfigurationRepository(): UiConfigurationRepository
}
