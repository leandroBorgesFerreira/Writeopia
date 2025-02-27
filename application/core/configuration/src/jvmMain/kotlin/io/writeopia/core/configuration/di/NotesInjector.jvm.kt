package io.writeopia.core.configuration.di

import io.writeopia.core.configuration.repository.ConfigurationRepository
import io.writeopia.core.configuration.repository.ConfigurationSqlDelightRepository
import io.writeopia.core.folders.repository.FolderRepository
import io.writeopia.models.configuration.WorkspaceConfigRepository
import io.writeopia.sql.WriteopiaDb
import io.writeopia.sqldelight.dao.ConfigurationSqlDelightDao
import io.writeopia.sqldelight.dao.FolderSqlDelightDao

actual class NotesInjector private constructor(
    private val writeopiaDb: WriteopiaDb?
) {
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

    actual fun provideFoldersRepository(): FolderRepository {
        TODO("Not yet implemented")
    }

    actual fun provideWorkspaceConfigRepository(): WorkspaceConfigRepository {
        TODO("Not yet implemented")
    }

    actual companion object {
        actual fun singleton(): NotesInjector {
            TODO("Not yet implemented")
        }
    }

}
