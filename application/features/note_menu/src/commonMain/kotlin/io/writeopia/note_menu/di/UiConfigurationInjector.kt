package io.writeopia.note_menu.di

import io.writeopia.note_menu.data.repository.UiConfigurationRepository

expect class UiConfigurationInjector {

    fun provideUiConfigurationRepository(): UiConfigurationRepository
}
