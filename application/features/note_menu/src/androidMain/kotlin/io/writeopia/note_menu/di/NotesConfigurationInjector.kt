package io.writeopia.note_menu.di

import io.writeopia.note_menu.data.repository.NotesConfigurationRepository
import io.writeopia.note_menu.data.repository.NotesConfigurationRoomRepository
import io.writeopia.persistence.room.injection.AppRoomDaosInjection

actual class NotesConfigurationInjector(private val appRoomDaosInjection: AppRoomDaosInjection) {

    actual fun provideNotesConfigurationRepository(): NotesConfigurationRepository =
        NotesConfigurationRoomRepository(appRoomDaosInjection.provideConfigurationDao())
}