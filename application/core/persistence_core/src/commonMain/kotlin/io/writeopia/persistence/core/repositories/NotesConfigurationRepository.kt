package io.writeopia.persistence.core.repositories

import io.writeopia.persistence.core.models.NotesArrangement
import io.writeopia.sdk.persistence.core.sorting.OrderBy

interface NotesConfigurationRepository {

    suspend fun saveDocumentArrangementPref(arrangement: NotesArrangement, userId: String)

    suspend fun saveDocumentSortingPref(orderBy: OrderBy, userId: String)

    suspend fun arrangementPref(userId: String): String

    suspend fun getOrderPreference(userId: String): String
}