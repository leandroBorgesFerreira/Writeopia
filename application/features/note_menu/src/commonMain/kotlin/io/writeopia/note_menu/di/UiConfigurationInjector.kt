package io.writeopia.note_menu.di

import io.writeopia.repository.UiConfigurationRepository

expect class UiConfigurationInjector {

    fun provideUiConfigurationRepository(): UiConfigurationRepository
}
