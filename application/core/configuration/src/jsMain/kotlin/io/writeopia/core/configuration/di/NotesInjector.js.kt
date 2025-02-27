package io.writeopia.core.configuration.di

import io.writeopia.core.configuration.repository.ConfigurationRepository
import io.writeopia.core.configuration.repository.InMemoryConfigurationRepository
import io.writeopia.core.folders.di.FolderInjector
import io.writeopia.core.folders.repository.FolderRepository
import io.writeopia.core.folders.repository.InMemoryFolderRepository
import io.writeopia.models.configuration.ConfigurationInjector
import io.writeopia.models.configuration.WorkspaceConfigRepository

actual class NotesInjector : FolderInjector, ConfigurationInjector {
    actual fun provideNotesConfigurationRepository(): ConfigurationRepository =
        InMemoryConfigurationRepository.singleton()

    actual override fun provideFoldersRepository(): FolderRepository =
        InMemoryFolderRepository.singleton()

    actual override fun provideWorkspaceConfigRepository(): WorkspaceConfigRepository {
        return provideNotesConfigurationRepository()
    }

    actual companion object {
        fun noop() = NotesInjector()

        actual fun singleton(): NotesInjector = noop()
    }
}
