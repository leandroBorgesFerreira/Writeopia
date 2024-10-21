package io.writeopia.note_menu.di

import io.writeopia.note_menu.data.repository.ConfigurationRepository
import io.writeopia.note_menu.data.repository.ConfigurationRoomRepository
import io.writeopia.note_menu.data.repository.FolderRepository
import io.writeopia.note_menu.data.repository.RoomFolderRepository
import io.writeopia.persistence.room.injection.AppRoomDaosInjection

actual class NotesInjector(private val appRoomDaosInjection: AppRoomDaosInjection) {

    private var configurationRepository: ConfigurationRepository? = null

    actual fun provideNotesConfigurationRepository(): ConfigurationRepository =
        configurationRepository ?: kotlin.run {
            ConfigurationRoomRepository(appRoomDaosInjection.provideConfigurationDao()).also {
                configurationRepository = it
            }
        }

    actual fun provideFoldersRepository(): FolderRepository =
        RoomFolderRepository(appRoomDaosInjection.provideFolderDao())
}
