package io.writeopia.sqldelight

import io.writeopia.app.sql.NotesConfiguration
import io.writeopia.app.sql.NotesConfigurationEntityQueries
import io.writeopia.sql.WriteopiaDb

class NotesConfigurationSqlDelightDao(database: WriteopiaDb) {

    private val notesConfigurationQueries: NotesConfigurationEntityQueries = database.notesConfigurationEntityQueries

    fun saveConfiguration(notesConfiguration: NotesConfiguration) {
        notesConfiguration.run {
            notesConfigurationQueries.insert(user_id, arrangement_type, order_by_type)
        }
    }

    fun getConfigurationByUserId(userId: String): NotesConfiguration? =
        notesConfigurationQueries.selectConfigurationByUserId(userId).executeAsOneOrNull()

}