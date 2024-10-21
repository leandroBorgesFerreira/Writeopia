package io.writeopia.note_menu.data.repository

import io.writeopia.note_menu.data.model.NotesArrangement
import io.writeopia.persistence.room.data.daos.NotesConfigurationRoomDao
import io.writeopia.persistence.room.data.entities.NotesConfigurationEntity
import io.writeopia.sdk.persistence.core.extensions.toEntityField
import io.writeopia.sdk.persistence.core.sorting.OrderBy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * This class is responsible to keep the information of the preferences or the user about the
 * notes, like orderBy (creation, last edition, name...) and arrangement (cards, list...).
 */
internal class ConfigurationRoomRepository(
    private val configurationDao: NotesConfigurationRoomDao
) : ConfigurationRepository {

    override suspend fun saveDocumentArrangementPref(
        arrangement: NotesArrangement,
        userId: String
    ) {
        val configuration =
            NotesConfigurationEntity(
                userId = userId,
                arrangementType = arrangement.type,
                orderByType = getOrderPreference(userId)
            )
        configurationDao.saveConfiguration(configuration)
    }

    override suspend fun saveDocumentSortingPref(orderBy: OrderBy, userId: String) {
        val configuration =
            NotesConfigurationEntity(
                userId = userId,
                arrangementType = arrangementPref(userId),
                orderByType = orderBy.type.toEntityField()
            )
        configurationDao.saveConfiguration(configuration)
    }

    override fun listenForArrangementPref(
        userId: String,
        coroutineScope: CoroutineScope?
    ): Flow<String> = configurationDao.listenForConfigurationByUserId(userId)
        .map { configuration ->
            configuration?.arrangementType ?: NotesArrangement.GRID.type
        }

    override fun listenOrderPreference(
        userId: String,
        coroutineScope: CoroutineScope?
    ): Flow<String> = configurationDao.listenForConfigurationByUserId(userId)
        .map { configuration ->
            configuration?.orderByType ?: OrderBy.CREATE.type
        }

    override suspend fun arrangementPref(userId: String): String =
        configurationDao.getConfigurationByUserId(userId)?.arrangementType
            ?: NotesArrangement.GRID.type

    override suspend fun getOrderPreference(userId: String): String =
        configurationDao.getConfigurationByUserId(userId)?.orderByType
            ?: OrderBy.CREATE.type.toEntityField()

    override suspend fun saveWorkspacePath(path: String, userId: String) {
    }

    override suspend fun loadWorkspacePath(userId: String): String? = null
}
