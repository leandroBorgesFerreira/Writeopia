package io.writeopia.notemenu.di

import io.writeopia.core.folders.di.FolderInjector
import io.writeopia.core.folders.repository.FolderRepository
import io.writeopia.notemenu.data.repository.ConfigurationRepository
import io.writeopia.notemenu.data.repository.InMemoryConfigurationRepository
import io.writeopia.core.folders.repository.InMemoryFolderRepository

actual class NotesInjector : FolderInjector {
    actual fun provideNotesConfigurationRepository(): ConfigurationRepository =
        InMemoryConfigurationRepository.singleton()

    actual override fun provideFoldersRepository(): FolderRepository = InMemoryFolderRepository.singleton()

    companion object {
        fun noop() = NotesInjector()
    }
}
