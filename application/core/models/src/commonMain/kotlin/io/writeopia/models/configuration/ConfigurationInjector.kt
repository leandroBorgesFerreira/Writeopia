package io.writeopia.models.configuration

interface ConfigurationInjector {

    fun provideWorkspaceConfigRepository(): WorkspaceConfigRepository
}
