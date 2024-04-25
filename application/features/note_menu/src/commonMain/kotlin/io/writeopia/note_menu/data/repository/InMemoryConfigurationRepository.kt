package io.writeopia.note_menu.data.repository

import io.writeopia.note_menu.data.NotesArrangement
import io.writeopia.sdk.persistence.core.sorting.OrderBy

class InMemoryConfigurationRepository private constructor(): ConfigurationRepository {

    private val arrangementPrefs = mutableMapOf<String, String>()
    private val sortPrefs = mutableMapOf<String, String>()
    private val workSpacePrefs = mutableMapOf<String, String>()

    override suspend fun saveDocumentArrangementPref(
        arrangement: NotesArrangement,
        userId: String
    ) {
        arrangementPrefs[userId] = arrangement.type
    }

    override suspend fun saveDocumentSortingPref(orderBy: OrderBy, userId: String) {
        sortPrefs[userId] = orderBy.type
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
