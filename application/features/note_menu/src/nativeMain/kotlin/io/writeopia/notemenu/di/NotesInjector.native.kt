package io.writeopia.notemenu.di

import io.writeopia.notemenu.data.repository.ConfigurationRepository
import io.writeopia.notemenu.data.repository.FolderRepository

actual class NotesInjector {
    actual fun provideNotesConfigurationRepository(): ConfigurationRepository {
        TODO("Not yet implemented")
    }

    actual fun provideFoldersRepository(): FolderRepository {
        TODO("Not yet implemented")
    }
}
