package io.writeopia.note_menu.di

import io.writeopia.note_menu.data.repository.ConfigurationRepository
import io.writeopia.note_menu.data.repository.ConfigurationSqlDelightRepository
import io.writeopia.sql.WriteopiaDb
import io.writeopia.sqldelight.ConfigurationSqlDelightDao

actual class NotesConfigurationInjector(private val writeopiaDb: WriteopiaDb?) {

    private fun provideNotesConfigurationSqlDelightDao() =
        ConfigurationSqlDelightDao(writeopiaDb)

    actual fun provideNotesConfigurationRepository(): ConfigurationRepository =
        ConfigurationSqlDelightRepository(provideNotesConfigurationSqlDelightDao())

    companion object {
        fun noop() = NotesConfigurationInjector(null)
    }
}
