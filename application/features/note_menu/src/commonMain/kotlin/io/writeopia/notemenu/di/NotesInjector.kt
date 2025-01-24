package io.writeopia.notemenu.di

import io.writeopia.core.folders.di.FolderInjector
import io.writeopia.core.folders.repository.FolderRepository
import io.writeopia.notemenu.data.repository.ConfigurationRepository

expect class NotesInjector : FolderInjector {

    fun provideNotesConfigurationRepository(): ConfigurationRepository

    override fun provideFoldersRepository(): FolderRepository
}
