package io.writeopia.note_menu.di

import io.writeopia.note_menu.data.repository.ConfigurationRepository
import io.writeopia.note_menu.data.repository.FolderRepository
import io.writeopia.note_menu.data.repository.InMemoryConfigurationRepository
import io.writeopia.note_menu.data.repository.InMemoryFolderRepository

actual class NotesInjector {
    actual fun provideNotesConfigurationRepository(): ConfigurationRepository =
        InMemoryConfigurationRepository.singleton()

    actual fun provideFoldersRepository(): FolderRepository = InMemoryFolderRepository.singleton()

    companion object {
        fun noop() = NotesInjector()
    }
}
