package io.writeopia.note_menu.di

import io.writeopia.persistence.room.WriteopiaApplicationDatabase
import io.writeopia.repository.UiConfigurationRepository
import io.writeopia.repository.UiConfigurationRoomRepository

actual class UiConfigurationInjector(private val database: WriteopiaApplicationDatabase) {



    actual fun provideUiConfigurationRepository(): UiConfigurationRepository =
        UiConfigurationRoomRepository(null)
}
