package io.writeopia.note_menu.di

import io.writeopia.note_menu.data.repository.ConfigurationRepository
import io.writeopia.note_menu.data.repository.ConfigurationSqlDelightRepository
import io.writeopia.note_menu.data.repository.FolderRepository
import io.writeopia.note_menu.data.repository.FolderRepositorySqlDelight
import io.writeopia.sql.WriteopiaDb
import io.writeopia.sqldelight.dao.ConfigurationSqlDelightDao
import io.writeopia.sqldelight.dao.FolderSqlDelightDao

actual class NotesInjector(private val writeopiaDb: WriteopiaDb) {

    private fun provideFolderSqlDelightDao() = FolderSqlDelightDao(writeopiaDb)

    private fun provideNotesConfigurationSqlDelightDao() =
        ConfigurationSqlDelightDao(writeopiaDb)

    actual fun provideNotesConfigurationRepository(): ConfigurationRepository =
        ConfigurationSqlDelightRepository(provideNotesConfigurationSqlDelightDao())

    actual fun provideFoldersRepository(): FolderRepository =
        FolderRepositorySqlDelight(provideFolderSqlDelightDao())
}
