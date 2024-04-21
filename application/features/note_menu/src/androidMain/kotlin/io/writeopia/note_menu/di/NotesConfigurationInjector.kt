package io.writeopia.note_menu.di

import io.writeopia.note_menu.data.repository.ConfigurationRepository
import io.writeopia.note_menu.data.repository.ConfigurationRoomRepository
import io.writeopia.persistence.room.injection.AppRoomDaosInjection

actual class NotesConfigurationInjector(private val appRoomDaosInjection: AppRoomDaosInjection) {

    actual fun provideNotesConfigurationRepository(): ConfigurationRepository =
        ConfigurationRoomRepository(appRoomDaosInjection.provideConfigurationDao())
}