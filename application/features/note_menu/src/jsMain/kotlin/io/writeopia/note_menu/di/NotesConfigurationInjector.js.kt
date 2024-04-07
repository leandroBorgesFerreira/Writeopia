package io.writeopia.note_menu.di

import io.writeopia.note_menu.data.repository.NotesConfigurationRepository
import io.writeopia.note_menu.data.repository.NotesConfigurationSqlDelightRepository
import io.writeopia.sql.WriteopiaDb
import io.writeopia.sqldelight.NotesConfigurationSqlDelightDao

actual class NotesConfigurationInjector(private val writeopiaDb: WriteopiaDb?) {

    private fun provideNotesConfigurationSqlDelightDao() =
        NotesConfigurationSqlDelightDao(writeopiaDb)

    actual fun provideNotesConfigurationRepository(): NotesConfigurationRepository =
        NotesConfigurationSqlDelightRepository(provideNotesConfigurationSqlDelightDao())

    companion object {
        fun noop() = NotesConfigurationInjector(null)
    }
}