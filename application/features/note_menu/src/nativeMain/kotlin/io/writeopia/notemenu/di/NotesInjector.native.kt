package io.writeopia.notemenu.di

import io.writeopia.core.folders.di.FolderInjector
import io.writeopia.core.folders.repository.FolderRepository
import io.writeopia.notemenu.data.repository.ConfigurationRepository
import io.writeopia.notemenu.data.repository.ConfigurationSqlDelightRepository
import io.writeopia.core.folders.repository.FolderRepositorySqlDelight
import io.writeopia.models.configuration.ConfigurationInjector
import io.writeopia.models.configuration.WorkspaceConfigRepository
import io.writeopia.sql.WriteopiaDb
import io.writeopia.sqldelight.dao.ConfigurationSqlDelightDao
import io.writeopia.sqldelight.dao.FolderSqlDelightDao

actual class NotesInjector(
    private val writeopiaDb: WriteopiaDb?
) : FolderInjector, ConfigurationInjector {

    private var configurationRepository: ConfigurationRepository? = null
    private var configurationSqlDelightDao: ConfigurationSqlDelightDao? = null

    private fun provideFolderSqlDelightDao() = FolderSqlDelightDao(writeopiaDb)

    private fun provideNotesConfigurationSqlDelightDao() =
        configurationSqlDelightDao ?: kotlin.run {
            ConfigurationSqlDelightDao(writeopiaDb).also {
                configurationSqlDelightDao = it
            }
        }

    actual fun provideNotesConfigurationRepository(): ConfigurationRepository =
        configurationRepository ?: kotlin.run {
            ConfigurationSqlDelightRepository(provideNotesConfigurationSqlDelightDao()).also {
                configurationRepository = it
            }
        }

    actual override fun provideFoldersRepository(): FolderRepository =
        FolderRepositorySqlDelight(provideFolderSqlDelightDao())

    actual override fun provideWorkspaceConfigRepository(): WorkspaceConfigRepository =
        provideNotesConfigurationRepository()
}
