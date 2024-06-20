package io.writeopia.note_menu.di

import io.writeopia.note_menu.data.repository.ConfigurationRepository
import io.writeopia.note_menu.data.repository.FolderRepository
import io.writeopia.note_menu.data.repository.InMemoryConfigurationRepository

actual class NotesInjector(
//    private val writeopiaDb: WriteopiaDb?
) {

//    private fun provideNotesConfigurationSqlDelightDao() =
//        if (writeopiaDb != null) ConfigurationSqlDelightDao(writeopiaDb) else null


    actual fun provideNotesConfigurationRepository(): ConfigurationRepository =
        InMemoryConfigurationRepository.singleton()

    actual fun provideFoldersRepository(): FolderRepository {

    }

    companion object {
        fun noop() = NotesInjector()
    }
}
