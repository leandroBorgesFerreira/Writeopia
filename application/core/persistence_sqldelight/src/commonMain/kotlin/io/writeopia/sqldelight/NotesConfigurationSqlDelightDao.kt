package io.writeopia.sqldelight

import app.cash.sqldelight.async.coroutines.awaitAsOneOrNull
import io.writeopia.app.sql.NotesConfiguration
import io.writeopia.app.sql.NotesConfigurationEntityQueries
import io.writeopia.sql.WriteopiaDb

class NotesConfigurationSqlDelightDao(database: WriteopiaDb) {

    private val notesConfigurationQueries: NotesConfigurationEntityQueries =
        database.notesConfigurationEntityQueries

    suspend fun saveConfiguration(notesConfiguration: NotesConfiguration) {
        notesConfiguration.run {
            notesConfigurationQueries.insert(user_id, arrangement_type, order_by)
        }
    }

    suspend fun getConfigurationByUserId(userId: String): NotesConfiguration? =
        notesConfigurationQueries.selectConfigurationByUserId(userId).awaitAsOneOrNull()

}