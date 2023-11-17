package io.writeopia.note_menu.data.repository

import io.writeopia.note_menu.data.NotesArrangement
import io.writeopia.sdk.persistence.core.sorting.OrderBy

interface NotesConfigurationRepository {

    suspend fun saveDocumentArrangementPref(arrangement: NotesArrangement, userId: String)

    suspend fun saveDocumentSortingPref(orderBy: OrderBy, userId: String)

    suspend fun arrangementPref(userId: String): String

    suspend fun getOrderPreference(userId: String): String
}