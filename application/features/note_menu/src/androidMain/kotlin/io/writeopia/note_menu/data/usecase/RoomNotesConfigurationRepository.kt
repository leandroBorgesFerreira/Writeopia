package io.writeopia.note_menu.data.usecase

import io.writeopia.note_menu.viewmodel.NotesArrangement
import io.writeopia.persistence.data.daos.NotesConfigurationDao
import io.writeopia.persistence.data.entities.NotesConfigurationEntity
import io.writeopia.sdk.persistence.core.extensions.toEntityField
import io.writeopia.sdk.persistence.core.sorting.OrderBy

/**
 * This class is responsible to keep the information of the preferences or the user about the
 * notes, like orderBy (creation, last edition, name...) and arrangement (cards, list...).
 */
internal class RoomNotesConfigurationRepository(private val configurationDao: NotesConfigurationDao) {

    suspend fun saveDocumentArrangementPref(arrangement: NotesArrangement) {
        val configuration = NotesConfigurationEntity(arrangementType = arrangement.type, orderByType = getOrderPreference())
        configurationDao.saveConfiguration(configuration)
    }

    suspend fun saveDocumentSortingPref(orderBy: OrderBy) {
        val configuration = NotesConfigurationEntity(arrangementType = arrangementPref(), orderByType = orderBy.type.toEntityField())
        configurationDao.saveConfiguration(configuration)
    }

    suspend fun arrangementPref(): String =
        configurationDao.getConfiguration()?.arrangementType ?: NotesArrangement.GRID.type

    suspend fun getOrderPreference(): String =
        configurationDao.getConfiguration()?.orderByType ?: OrderBy.CREATE.type.toEntityField()
}