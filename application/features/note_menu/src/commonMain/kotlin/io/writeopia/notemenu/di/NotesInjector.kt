package io.writeopia.notemenu.di

import io.writeopia.core.configuration.repository.ConfigurationRepository
import io.writeopia.core.folders.di.FolderInjector
import io.writeopia.core.folders.repository.FolderRepository
import io.writeopia.models.configuration.ConfigurationInjector
import io.writeopia.models.configuration.WorkspaceConfigRepository

expect class NotesInjector : FolderInjector, ConfigurationInjector {

    fun provideNotesConfigurationRepository(): ConfigurationRepository

    override fun provideFoldersRepository(): FolderRepository

    override fun provideWorkspaceConfigRepository(): WorkspaceConfigRepository

    companion object {
        fun singleton(): NotesInjector
    }
}
