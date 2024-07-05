package io.writeopia.note_menu.di

import io.writeopia.note_menu.data.repository.ConfigurationRepository
import io.writeopia.note_menu.data.repository.FolderRepository

expect class NotesInjector {

    fun provideNotesConfigurationRepository(): ConfigurationRepository

    fun provideFoldersRepository(): FolderRepository
}
