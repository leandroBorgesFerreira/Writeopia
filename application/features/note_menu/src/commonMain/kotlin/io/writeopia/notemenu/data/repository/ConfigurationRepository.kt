package io.writeopia.notemenu.data.repository

import io.writeopia.notemenu.data.model.NotesArrangement
import io.writeopia.sdk.persistence.core.sorting.OrderBy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface ConfigurationRepository {

    suspend fun saveDocumentArrangementPref(arrangement: NotesArrangement, userId: String)

    suspend fun saveDocumentSortingPref(orderBy: OrderBy, userId: String)

    suspend fun arrangementPref(userId: String): String

    suspend fun getOrderPreference(userId: String): String

    suspend fun listenForArrangementPref(userId: String): Flow<String>

    suspend fun listenOrderPreference(userId: String): Flow<String>

    suspend fun saveWorkspacePath(path: String, userId: String)

    suspend fun loadWorkspacePath(userId: String): String?
}
