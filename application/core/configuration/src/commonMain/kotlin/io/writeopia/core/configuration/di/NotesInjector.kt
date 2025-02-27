package io.writeopia.core.configuration.di

import io.writeopia.core.configuration.repository.ConfigurationRepository
import io.writeopia.models.configuration.WorkspaceConfigRepository

expect class NotesInjector {

    fun provideNotesConfigurationRepository(): ConfigurationRepository

    fun provideWorkspaceConfigRepository(): WorkspaceConfigRepository

    companion object {
        fun singleton(): NotesInjector
    }
}
