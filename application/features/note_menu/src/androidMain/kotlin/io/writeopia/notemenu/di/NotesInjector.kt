package io.writeopia.notemenu.di

import io.writeopia.notemenu.data.repository.ConfigurationRepository
import io.writeopia.notemenu.data.repository.ConfigurationRoomRepository
import io.writeopia.notemenu.data.repository.RoomFolderRepository
import io.writeopia.common.utils.persistence.di.AppDaosInjection
import io.writeopia.core.folders.repository.FolderRepository

actual class NotesInjector(private val appRoomDaosInjection: AppDaosInjection) {

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
