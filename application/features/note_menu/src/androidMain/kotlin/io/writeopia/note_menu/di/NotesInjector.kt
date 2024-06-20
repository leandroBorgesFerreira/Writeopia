package io.writeopia.note_menu.di

import io.writeopia.note_menu.data.repository.ConfigurationRepository
import io.writeopia.note_menu.data.repository.ConfigurationRoomRepository
import io.writeopia.note_menu.data.repository.FolderRepository
import io.writeopia.persistence.room.injection.AppRoomDaosInjection

actual class NotesInjector(private val appRoomDaosInjection: AppRoomDaosInjection) {

    actual fun provideNotesConfigurationRepository(): ConfigurationRepository =
        ConfigurationRoomRepository(appRoomDaosInjection.provideConfigurationDao())

    actual fun provideFoldersRepository(): FolderRepository {

    }
}
