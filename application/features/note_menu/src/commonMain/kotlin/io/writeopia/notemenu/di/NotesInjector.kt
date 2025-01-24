package io.writeopia.notemenu.di

import io.writeopia.core.folders.FolderRepository
import io.writeopia.notemenu.data.repository.ConfigurationRepository

expect class NotesInjector {

    fun provideNotesConfigurationRepository(): ConfigurationRepository

    fun provideFoldersRepository(): FolderRepository
}
