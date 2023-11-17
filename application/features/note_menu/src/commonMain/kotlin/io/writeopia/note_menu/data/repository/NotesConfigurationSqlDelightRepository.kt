package io.writeopia.note_menu.data.repository

import io.writeopia.app.sql.NotesConfiguration
import io.writeopia.note_menu.data.NotesArrangement
import io.writeopia.sdk.persistence.core.sorting.OrderBy
import io.writeopia.sqldelight.NotesConfigurationSqlDelightDao

class NotesConfigurationSqlDelightRepository(
    private val notesConfigurationSqlDelightDao: NotesConfigurationSqlDelightDao
) : NotesConfigurationRepository {

    override suspend fun saveDocumentArrangementPref(arrangement: NotesArrangement, userId: String) {
        notesConfigurationSqlDelightDao.saveConfiguration(
            NotesConfiguration(
                userId,
                arrangement.type,
                arrangementPref(userId)
            )
        )
    }

    override suspend fun saveDocumentSortingPref(orderBy: OrderBy, userId: String) {
        notesConfigurationSqlDelightDao.saveConfiguration(
            NotesConfiguration(
                userId,
                getOrderPreference(userId),
                orderBy.type
            )
        )
    }

    override suspend fun arrangementPref(userId: String): String =
        notesConfigurationSqlDelightDao.getConfigurationByUserId(userId)?.arrangement_type
            ?: NotesArrangement.GRID.type

    override suspend fun getOrderPreference(userId: String): String =
        notesConfigurationSqlDelightDao.getConfigurationByUserId(userId)?.order_by ?: NotesArrangement.GRID.type
}