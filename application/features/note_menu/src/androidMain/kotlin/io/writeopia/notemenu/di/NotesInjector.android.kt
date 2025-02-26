package io.writeopia.notemenu.di

import io.writeopia.common.utils.persistence.di.AppDaosInjection
import io.writeopia.core.folders.di.FolderInjector
import io.writeopia.core.folders.repository.FolderRepository
import io.writeopia.core.folders.repository.RoomFolderRepository
import io.writeopia.models.configuration.ConfigurationInjector
import io.writeopia.models.configuration.WorkspaceConfigRepository
import io.writeopia.notemenu.data.repository.ConfigurationRepository
import io.writeopia.notemenu.data.repository.ConfigurationRoomRepository
import io.writeopia.persistence.room.injection.AppRoomDaosInjection

actual class NotesInjector private constructor(
    private val appRoomDaosInjection: AppDaosInjection
) : FolderInjector, ConfigurationInjector {

    private var configurationRepository: ConfigurationRepository? = null

    actual fun provideNotesConfigurationRepository(): ConfigurationRepository =
        configurationRepository ?: kotlin.run {
            ConfigurationRoomRepository(appRoomDaosInjection.provideConfigurationDao()).also {
                configurationRepository = it
            }
        }

    actual override fun provideFoldersRepository(): FolderRepository =
        RoomFolderRepository(appRoomDaosInjection.provideFolderDao())

    actual override fun provideWorkspaceConfigRepository(): WorkspaceConfigRepository =
        provideNotesConfigurationRepository()

    actual companion object {
        private var instance: NotesInjector? = null

        actual fun singleton(): NotesInjector =
            instance ?: NotesInjector(AppRoomDaosInjection.singleton()).also {
                instance = it
            }
    }
}
