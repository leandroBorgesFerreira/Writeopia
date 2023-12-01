package io.writeopia.note_menu.viewmodel

import io.writeopia.note_menu.data.NotesArrangement
import io.writeopia.note_menu.ui.dto.DocumentUi
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

    fun requestDocuments(force: Boolean)

    suspend fun requestUser()

    fun editMenu()

    fun cancelMenu()

    fun onDocumentSelected(id: String, selected: Boolean)

    fun clearSelection()

    fun listArrangementSelected()

    fun gridArrangementSelected()

    fun sortingSelected(orderBy: OrderBy)

    fun copySelectedNotes()

    fun deleteSelectedNotes()

    fun favoriteSelectedNotes()
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
