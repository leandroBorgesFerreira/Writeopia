package io.writeopia.note_menu.di

import io.writeopia.note_menu.data.repository.ConfigurationRepository
import io.writeopia.note_menu.data.repository.FolderRepository

actual class NotesInjector {
    actual fun provideNotesConfigurationRepository(): ConfigurationRepository {
        TODO("Not yet implemented")
    }

    actual fun provideFoldersRepository(): FolderRepository {
        TODO("Not yet implemented")
    }

}
