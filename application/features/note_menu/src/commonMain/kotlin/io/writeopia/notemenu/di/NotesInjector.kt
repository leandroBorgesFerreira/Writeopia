package io.writeopia.notemenu.di

import io.writeopia.notemenu.data.repository.ConfigurationRepository
import io.writeopia.notemenu.data.repository.FolderRepository

expect class NotesInjector {

    fun provideNotesConfigurationRepository(): ConfigurationRepository

    fun provideFoldersRepository(): FolderRepository
}
