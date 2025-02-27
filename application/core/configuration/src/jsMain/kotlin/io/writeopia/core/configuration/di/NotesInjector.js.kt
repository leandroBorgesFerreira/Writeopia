package io.writeopia.core.configuration.di

import io.writeopia.core.configuration.repository.ConfigurationRepository
import io.writeopia.core.configuration.repository.InMemoryConfigurationRepository
import io.writeopia.core.folders.repository.FolderRepository
import io.writeopia.core.folders.repository.InMemoryFolderRepository
import io.writeopia.models.configuration.WorkspaceConfigRepository

actual class NotesInjector {
    actual fun provideNotesConfigurationRepository(): ConfigurationRepository =
        InMemoryConfigurationRepository.singleton()

    actual fun provideWorkspaceConfigRepository(): WorkspaceConfigRepository {
        return provideNotesConfigurationRepository()
    }

    actual companion object {
        private fun noop() = NotesInjector()

        actual fun singleton(): NotesInjector = noop()
    }
}
