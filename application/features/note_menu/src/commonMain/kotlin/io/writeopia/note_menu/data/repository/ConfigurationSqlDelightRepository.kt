package io.writeopia.note_menu.data.repository

import io.writeopia.app.sql.NotesConfiguration
import io.writeopia.app.sql.WorkspaceConfiguration
import io.writeopia.note_menu.data.model.NotesArrangement
import io.writeopia.sdk.persistence.core.sorting.OrderBy
import io.writeopia.sqldelight.dao.ConfigurationSqlDelightDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ConfigurationSqlDelightRepository(
    private val configurationSqlDelightDao: ConfigurationSqlDelightDao
) : ConfigurationRepository {

    private val _arrangementPref: MutableStateFlow<String> =
        MutableStateFlow(NotesArrangement.GRID.type)
    private val _orderPreference: MutableStateFlow<String> = MutableStateFlow(OrderBy.CREATE.type)

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

        refreshArrangementPref(userId)
    }

    override suspend fun saveDocumentSortingPref(orderBy: OrderBy, userId: String) {
        configurationSqlDelightDao.saveNotesConfiguration(
            NotesConfiguration(
                userId,
                arrangementPref(userId),
                orderBy.type
            )
        )

        refreshOrderPref(userId)
    }

    override suspend fun arrangementPref(userId: String): String =
        configurationSqlDelightDao.getConfigurationByUserId(userId)?.arrangement_type
            ?: NotesArrangement.GRID.type

    override suspend fun getOrderPreference(userId: String): String =
        configurationSqlDelightDao.getConfigurationByUserId(userId)?.order_by
            ?: OrderBy.CREATE.type

    override fun listenForArrangementPref(
        userId: String,
        coroutineScope: CoroutineScope?
    ): Flow<String> {
        coroutineScope?.launch {
            refreshArrangementPref(userId)
        }

        return _arrangementPref
    }

    override fun listenOrderPreference(
        userId: String,
        coroutineScope: CoroutineScope?
    ): Flow<String> {
        coroutineScope?.launch {
            refreshOrderPref(userId)
        }

        return _orderPreference
    }

    override suspend fun saveWorkspacePath(path: String, userId: String) {
        configurationSqlDelightDao.saveWorkspaceConfiguration(
            WorkspaceConfiguration(path = path, user_id = userId)
        )
    }

    override suspend fun loadWorkspacePath(userId: String): String? =
        configurationSqlDelightDao.getWorkspaceByUserId(userId)?.path

    private suspend fun refreshArrangementPref(userId: String) {
        _arrangementPref.value = arrangementPref(userId)
    }

    private suspend fun refreshOrderPref(userId: String) {
        _orderPreference.value = getOrderPreference(userId)
    }
}
