package io.writeopia.notemenu.di

import io.writeopia.notemenu.data.repository.ConfigurationRepository
import io.writeopia.notemenu.data.repository.FolderRepository
import io.writeopia.notemenu.data.repository.FolderRepositorySqlDelight
import io.writeopia.notemenu.data.repository.InMemoryConfigurationRepository
import io.writeopia.sql.WriteopiaDb
import io.writeopia.sqldelight.dao.ConfigurationSqlDelightDao
import io.writeopia.sqldelight.dao.FolderSqlDelightDao

actual class NotesInjector(private val writeopiaDb: WriteopiaDb?) {

    private var configurationRepository: ConfigurationRepository? = null

    private fun provideFolderSqlDelightDao() = FolderSqlDelightDao(writeopiaDb)

    private fun provideNotesConfigurationSqlDelightDao() =
        ConfigurationSqlDelightDao(writeopiaDb)

    actual fun provideNotesConfigurationRepository(): ConfigurationRepository =
        configurationRepository ?: kotlin.run {
            InMemoryConfigurationRepository.singleton().also {
                configurationRepository = it
            }
        }

    actual fun provideFoldersRepository(): FolderRepository =
        FolderRepositorySqlDelight(provideFolderSqlDelightDao())
}
