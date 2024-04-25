package io.writeopia.note_menu.di

import io.writeopia.note_menu.data.repository.ConfigurationRepository
import io.writeopia.note_menu.data.repository.ConfigurationSqlDelightRepository
import io.writeopia.note_menu.data.repository.InMemoryConfigurationRepository
import io.writeopia.sql.WriteopiaDb
import io.writeopia.sqldelight.ConfigurationSqlDelightDao

actual class NotesConfigurationInjector(
//    private val writeopiaDb: WriteopiaDb?
) {

//    private fun provideNotesConfigurationSqlDelightDao() =
//        if (writeopiaDb != null) ConfigurationSqlDelightDao(writeopiaDb) else null


    actual fun provideNotesConfigurationRepository(): ConfigurationRepository =
        InMemoryConfigurationRepository.singleton()

    companion object {
        fun noop() = NotesConfigurationInjector()
    }
}
