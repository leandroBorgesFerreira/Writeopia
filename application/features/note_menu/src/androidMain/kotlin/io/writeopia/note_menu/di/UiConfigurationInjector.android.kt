package io.writeopia.note_menu.di

import io.writeopia.repository.UiConfigurationRepository
import io.writeopia.repository.UiConfigurationRoomRepository

actual class UiConfigurationInjector {
    actual fun provideUiConfigurationRepository(): UiConfigurationRepository =
        UiConfigurationRoomRepository()
}
