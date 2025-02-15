package io.writeopia.notemenu.data.repository

import io.writeopia.common.utils.persistence.configuration.NotesConfigurationCommonEntity
import io.writeopia.common.utils.persistence.daos.NotesConfigurationCommonDao
import io.writeopia.notemenu.data.model.NotesArrangement
import io.writeopia.sdk.persistence.core.extensions.toEntityField
import io.writeopia.sdk.persistence.core.sorting.OrderBy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * This class is responsible to keep the information of the preferences or the user about the
 * notes, like orderBy (creation, last edition, name...) and arrangement (cards, list...).
 */
internal class ConfigurationRoomRepository(
    private val configurationDao: NotesConfigurationCommonDao
) : ConfigurationRepository {

    override suspend fun saveDocumentArrangementPref(
        arrangement: NotesArrangement,
        userId: String
    ) {
        val configuration =
            NotesConfigurationCommonEntity(
                userId = userId,
                arrangementType = arrangement.type,
                orderByType = getOrderPreference(userId),
                hasTutorials = true
            )
        configurationDao.saveConfiguration(configuration)
    }

    override suspend fun saveDocumentSortingPref(orderBy: OrderBy, userId: String) {
        val configuration =
            NotesConfigurationCommonEntity(
                userId = userId,
                arrangementType = arrangementPref(userId),
                orderByType = orderBy.type.toEntityField(),
                hasTutorials = true
            )
        configurationDao.saveConfiguration(configuration)
    }

    override suspend fun listenForArrangementPref(
        userId: String
    ): Flow<String> = configurationDao.listenForConfigurationByUserId(userId)
        .map { configuration ->
            configuration?.arrangementType ?: NotesArrangement.GRID.type
        }

    override suspend fun listenOrderPreference(
        userId: String
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

    override suspend fun hasTutorialNotes(userId: String): Boolean =
        configurationDao.getConfigurationByUserId(userId)?.hasTutorials ?: false

    override suspend fun setTutorialNotes(hasTutorials: Boolean, userId: String) {
        val configEntity = configurationDao.getConfigurationByUserId(userId)
            ?.copy(hasTutorials = hasTutorials)
            ?: NotesConfigurationCommonEntity(
                userId = userId,
                arrangementType = NotesArrangement.STAGGERED_GRID.type,
                orderByType = OrderBy.UPDATE.type,
                hasTutorials = hasTutorials
            )

        configurationDao.saveConfiguration(configEntity)
    }
}
