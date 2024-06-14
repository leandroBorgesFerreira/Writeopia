package io.writeopia.note_menu.data.repository

import io.writeopia.app.sql.NotesConfiguration
import io.writeopia.app.sql.WorkspaceConfiguration
import io.writeopia.note_menu.data.model.NotesArrangement
import io.writeopia.sdk.persistence.core.sorting.OrderBy
import io.writeopia.sqldelight.ConfigurationSqlDelightDao

class ConfigurationSqlDelightRepository(
    private val configurationSqlDelightDao: ConfigurationSqlDelightDao
) : ConfigurationRepository {

    override suspend fun saveDocumentArrangementPref(
        arrangement: NotesArrangement,
        userId: String
    ) {
        configurationSqlDelightDao.saveNotesConfiguration(
            NotesConfiguration(
                userId,
                arrangement.type,
                getOrderPreference(userId)
            )
        )
    }

    override suspend fun saveDocumentSortingPref(orderBy: OrderBy, userId: String) {
        configurationSqlDelightDao.saveNotesConfiguration(
            NotesConfiguration(
                userId,
                arrangementPref(userId),
                orderBy.type
            )
        )
    }

    override suspend fun arrangementPref(userId: String): String =
        configurationSqlDelightDao.getConfigurationByUserId(userId)?.arrangement_type
            ?: NotesArrangement.GRID.type

    override suspend fun getOrderPreference(userId: String): String =
        configurationSqlDelightDao.getConfigurationByUserId(userId)?.order_by
            ?: OrderBy.CREATE.type

    override suspend fun saveWorkspacePath(path: String, userId: String) {
        configurationSqlDelightDao.saveWorkspaceConfiguration(
            WorkspaceConfiguration(path = path, user_id = userId)
        )
    }

    override suspend fun loadWorkspacePath(userId: String): String? =
        configurationSqlDelightDao.getWorkspaceByUserId(userId)?.path
}
