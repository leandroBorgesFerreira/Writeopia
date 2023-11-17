package io.writeopia.note_menu.data.repository

import io.writeopia.note_menu.data.NotesArrangement
import io.writeopia.sdk.persistence.core.sorting.OrderBy

class NotesConfigurationSqlDelightRepository: NotesConfigurationRepository {

    override suspend fun saveDocumentArrangementPref(arrangement: NotesArrangement, userId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun saveDocumentSortingPref(orderBy: OrderBy, userId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun arrangementPref(userId: String): String {
        TODO("Not yet implemented")
    }

    override suspend fun getOrderPreference(userId: String): String {
        TODO("Not yet implemented")
    }
}