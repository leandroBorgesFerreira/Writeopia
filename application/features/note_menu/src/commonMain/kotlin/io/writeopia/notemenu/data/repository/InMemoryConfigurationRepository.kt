package io.writeopia.notemenu.data.repository

import io.writeopia.notemenu.data.model.NotesArrangement
import io.writeopia.sdk.persistence.core.sorting.OrderBy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class InMemoryConfigurationRepository private constructor() : ConfigurationRepository {

    private val arrangementPrefs = mutableMapOf<String, String>()
    private val sortPrefs = mutableMapOf<String, String>()
    private val workSpacePrefs = mutableMapOf<String, String>()

    private val arrangementPrefsState = MutableStateFlow(NotesArrangement.STAGGERED_GRID.type)
    private val sortPrefsState = MutableStateFlow(OrderBy.UPDATE.type)

    override suspend fun saveDocumentArrangementPref(
        arrangement: NotesArrangement,
        userId: String
    ) {
        arrangementPrefs[userId] = arrangement.type
        arrangementPrefsState.value = arrangement.type
    }

    override suspend fun saveDocumentSortingPref(orderBy: OrderBy, userId: String) {
        sortPrefs[userId] = orderBy.type
        sortPrefsState.value = orderBy.type
    }

    override suspend fun arrangementPref(userId: String): String =
        arrangementPrefs[userId] ?: NotesArrangement.GRID.type

    override suspend fun getOrderPreference(userId: String): String =
        sortPrefs[userId] ?: OrderBy.NAME.type

    override suspend fun saveWorkspacePath(path: String, userId: String) {
        workSpacePrefs[userId] = path
    }

    override suspend fun loadWorkspacePath(userId: String): String? =
        workSpacePrefs[userId]

    override fun listenForArrangementPref(
        userId: String,
        coroutineScope: CoroutineScope?
    ): Flow<String> = arrangementPrefsState.asStateFlow()

    override fun listenOrderPreference(
        userId: String,
        coroutineScope: CoroutineScope?
    ): Flow<String> = sortPrefsState.asStateFlow()

    companion object {
        private var instance: InMemoryConfigurationRepository? = null

        fun singleton(): InMemoryConfigurationRepository {
            return instance ?: run {
                instance = InMemoryConfigurationRepository()
                instance!!
            }
        }
    }
}
