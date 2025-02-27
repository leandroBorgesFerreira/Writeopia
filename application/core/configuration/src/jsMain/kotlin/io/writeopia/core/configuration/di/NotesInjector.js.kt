package io.writeopia.core.configuration.di

import io.writeopia.core.configuration.repository.ConfigurationRepository
import io.writeopia.core.folders.repository.FolderRepository
import io.writeopia.models.configuration.WorkspaceConfigRepository

actual class NotesInjector {
    actual fun provideNotesConfigurationRepository(): ConfigurationRepository {
        TODO("Not yet implemented")
    }

    actual fun provideFoldersRepository(): FolderRepository {
        TODO("Not yet implemented")
    }

    actual fun provideWorkspaceConfigRepository(): WorkspaceConfigRepository {
        TODO("Not yet implemented")
    }

    actual companion object {
        actual fun singleton(): NotesInjector {
            TODO("Not yet implemented")
        }
    }

}
