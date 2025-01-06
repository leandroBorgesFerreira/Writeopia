package io.writeopia.notemenu.di

import io.writeopia.notemenu.data.repository.ConfigurationRepository
import io.writeopia.notemenu.data.repository.FolderRepository
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
