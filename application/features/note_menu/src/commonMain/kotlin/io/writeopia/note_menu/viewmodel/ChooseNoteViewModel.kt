package io.writeopia.note_menu.viewmodel

import io.writeopia.note_menu.data.NotesArrangement
import io.writeopia.note_menu.ui.dto.NotesUi
import io.writeopia.sdk.persistence.core.sorting.OrderBy
import io.writeopia.utils_module.ResultData
import kotlinx.coroutines.flow.StateFlow

interface ChooseNoteViewModel {

    val hasSelectedNotes: StateFlow<Boolean>

    val userName: StateFlow<UserState<String>>

    val documentsState: StateFlow<ResultData<NotesUi>>

    val notesArrangement: StateFlow<NotesArrangement?>

    val editState: StateFlow<Boolean>

    val showSortMenuState: StateFlow<Boolean>

    val showLocalSyncConfigState: StateFlow<ConfigState>

    val syncInProgress: StateFlow<SyncState>

    fun requestDocuments(force: Boolean)

    fun handleNoteTap(id: String): Boolean

    suspend fun requestUser()

    fun showEditMenu()

    fun showSortMenu()

    fun cancelEditMenu()

    fun cancelSortMenu()

    fun directoryFilesAsMarkdown(path: String)

    fun loadFiles(filePaths: List<String>)

    fun onDocumentSelected(id: String, selected: Boolean)

    fun onSyncLocallySelected()

    fun configureDirectory()

    fun onWriteLocallySelected()

    fun clearSelection()

    fun listArrangementSelected()

    fun gridArrangementSelected()

    fun sortingSelected(orderBy: OrderBy)

    fun copySelectedNotes()

    fun deleteSelectedNotes()

    fun favoriteSelectedNotes()

    fun hideConfigSyncMenu()

    fun pathSelected(path: String)

    fun confirmWorkplacePath()
}

sealed interface UserState<T> {
    class Idle<T> : UserState<T>
    class Loading<T> : UserState<T>
    class ConnectedUser<T>(val data: T) : UserState<T>
    class UserNotReturned<T> : UserState<T>
    class DisconnectedUser<T>(val data: T) : UserState<T>
}

fun <T, R> UserState<T>.map(fn: (T) -> R): UserState<R> =
    when (this) {
        is UserState.ConnectedUser -> UserState.ConnectedUser(fn(this.data))
        is UserState.Idle -> UserState.Idle()
        is UserState.Loading -> UserState.Loading()
        is UserState.DisconnectedUser -> UserState.DisconnectedUser(fn(this.data))
        is UserState.UserNotReturned -> UserState.UserNotReturned()
    }

sealed interface SyncState {
    data object LoadingSync : SyncState

    data object LoadingWrite : SyncState

    data object Idle : SyncState
}

sealed interface ConfigState {

    data class Configure(val path: String, val syncRequest: SyncRequest) : ConfigState

    data object Idle : ConfigState
}

enum class SyncRequest {
    WRITE, READ_WRITE, CONFIGURE
}

fun ConfigState.setPath(func: () -> String): ConfigState =
    when (this) {
        is ConfigState.Configure -> ConfigState.Configure(func(), this.syncRequest)
        ConfigState.Idle -> ConfigState.Idle
    }

fun ConfigState.getPath(): String? =
    when (this) {
        is ConfigState.Configure -> this.path
        ConfigState.Idle -> null
    }

fun ConfigState.getSyncRequest(): SyncRequest? =
    when (this) {
        is ConfigState.Configure -> syncRequest
        ConfigState.Idle -> null
    }