package io.writeopia.notemenu.di

import io.writeopia.core.folders.FolderRepository
import io.writeopia.notemenu.data.repository.ConfigurationRepository
import io.writeopia.notemenu.data.repository.InMemoryConfigurationRepository
import io.writeopia.notemenu.data.repository.InMemoryFolderRepository

actual class NotesInjector {
    actual fun provideNotesConfigurationRepository(): ConfigurationRepository =
        InMemoryConfigurationRepository.singleton()

    actual fun provideFoldersRepository(): FolderRepository = InMemoryFolderRepository.singleton()

    companion object {
        fun noop() = NotesInjector()
    }
}
