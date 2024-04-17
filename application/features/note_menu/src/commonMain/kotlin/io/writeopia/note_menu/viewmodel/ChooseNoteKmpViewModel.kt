package io.writeopia.note_menu.viewmodel

import io.writeopia.auth.core.data.User
import io.writeopia.auth.core.manager.AuthManager
import io.writeopia.note_menu.data.NotesArrangement
import io.writeopia.note_menu.data.repository.NotesConfigurationRepository
import io.writeopia.note_menu.data.usecase.NotesUseCase
import io.writeopia.note_menu.extensions.toUiCard
import io.writeopia.note_menu.ui.dto.NotesUi
import io.writeopia.sdk.export.DocumentToJson
import io.writeopia.sdk.export.DocumentToMarkdown
import io.writeopia.sdk.export.DocumentWriter
import io.writeopia.sdk.models.document.Document
import io.writeopia.sdk.persistence.core.sorting.OrderBy
import io.writeopia.sdk.preview.PreviewParser
import io.writeopia.utils_module.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

internal class ChooseNoteKmpViewModel(
    private val notesUseCase: NotesUseCase,
    private val notesConfig: NotesConfigurationRepository,
    private val authManager: AuthManager,
    private val previewParser: PreviewParser = PreviewParser(),
    private val documentToMarkdown: DocumentToMarkdown = DocumentToMarkdown,
    private val documentToJson: DocumentToJson = DocumentToJson()
) : ChooseNoteViewModel, KmpViewModel {

    private var localUserId: String? = null

    private lateinit var coroutineScope: CoroutineScope

    private val _selectedNotes = MutableStateFlow(setOf<String>())
    override val hasSelectedNotes: StateFlow<Boolean> by lazy {
        _selectedNotes.map { selectedIds ->
            selectedIds.isNotEmpty()
        }.stateIn(coroutineScope, SharingStarted.Lazily, false)
    }

    private val _documentsState: MutableStateFlow<ResultData<List<Document>>> =
        MutableStateFlow(ResultData.Idle())

    private val _user: MutableStateFlow<UserState<User>> = MutableStateFlow(UserState.Idle())
    override val userName: StateFlow<UserState<String>> by lazy {
        _user.map { userState ->
            userState.map { user ->
                user.name
            }
        }.stateIn(coroutineScope, SharingStarted.Lazily, UserState.Idle())
    }

    private val _notesArrangement = MutableStateFlow(NotesArrangement.GRID)
    override val notesArrangement: StateFlow<NotesArrangement> = _notesArrangement.asStateFlow()

    override val documentsState: StateFlow<ResultData<NotesUi>> by lazy {
        combine(
            _selectedNotes,
            _documentsState,
            notesArrangement
        ) { selectedNoteIds, resultData, arrangement ->
            resultData.map { documentList ->
                NotesUi(
                    documentUiList = documentList.map { document ->
                        document.toUiCard(previewParser, selectedNoteIds.contains(document.id))
                    },
                    notesArrangement = arrangement
                )

            }
        }.stateIn(coroutineScope, SharingStarted.Lazily, ResultData.Idle())
    }

    private val _editState = MutableStateFlow(false)
    override val editState: StateFlow<Boolean> = _editState.asStateFlow()

    private suspend fun getUserId(): String =
        localUserId ?: authManager.getUser().id.also { id ->
            localUserId = id
        }

    override fun initCoroutine(coroutineScope: CoroutineScope) {
        this.coroutineScope = coroutineScope
    }

    override fun requestDocuments(force: Boolean) {
        if (documentsState.value !is ResultData.Complete || force) {
            coroutineScope.launch(Dispatchers.Default) {
                refreshNotes()
            }
        }
    }

    override suspend fun requestUser() {
        try {
            _user.value = if (authManager.isLoggedIn().toBoolean()) {
                val user = authManager.getUser()

                if (user.id != DISCONNECTED_USER_ID) {
                    UserState.ConnectedUser(user)
                } else {
                    UserState.UserNotReturned()
                }
            } else {
                UserState.DisconnectedUser(User.disconnectedUser())
            }
        } catch (error: Exception) {
//            Log.d("ChooseNoteViewModel", "Error fetching user attributes. Error: $error")
        }
    }

    override fun showEditMenu() {
        _editState.value = !editState.value
    }

    override fun cancelEditMenu() {
        _editState.value = false
    }

    override fun onDocumentSelected(id: String, selected: Boolean) {
        coroutineScope.launch(Dispatchers.Default) {
            val selectedIds = _selectedNotes.value
            val newIds = if (selected) selectedIds + id else selectedIds - id

            _selectedNotes.value = newIds
        }
    }

    override fun clearSelection() {
        _selectedNotes.value = emptySet()
    }

    override fun listArrangementSelected() {
        coroutineScope.launch {
            notesConfig.saveDocumentArrangementPref(NotesArrangement.LIST, getUserId())
            _notesArrangement.value = NotesArrangement.LIST
        }
    }

    override fun gridArrangementSelected() {
        coroutineScope.launch {
            notesConfig.saveDocumentArrangementPref(NotesArrangement.GRID, getUserId())
            _notesArrangement.value = NotesArrangement.GRID
        }
    }

    override fun sortingSelected(orderBy: OrderBy) {
        coroutineScope.launch(Dispatchers.Default) {
            notesConfig.saveDocumentSortingPref(orderBy, getUserId())
            refreshNotes()
        }
    }

    override fun copySelectedNotes() {
        coroutineScope.launch(Dispatchers.Default) {
            notesUseCase.duplicateDocuments(_selectedNotes.value.toList(), getUserId())
            clearSelection()
            refreshNotes()
        }
    }

    override fun deleteSelectedNotes() {
        val selected = _selectedNotes.value

        coroutineScope.launch(Dispatchers.Default) {
            notesUseCase.deleteNotes(selected)
            clearSelection()
            refreshNotes()
        }
    }

    override fun favoriteSelectedNotes() {

    }

    override fun directoryFilesAsMarkdown(path: String) {
        directoryFilesAs(path, documentToMarkdown)
    }

    override fun directoryFilesAsJson(path: String) {
        directoryFilesAs(path, documentToJson)
    }

    private fun directoryFilesAs(path: String, documentWriter: DocumentWriter) {
        coroutineScope.launch(Dispatchers.Default) {
            val data = notesUseCase.loadDocumentsForUser(getUserId())
            documentWriter.writeDocuments(data, path)

            cancelEditMenu()
        }
    }

    private suspend fun refreshNotes() {
        _documentsState.value = ResultData.Loading()

        try {
            val data = notesUseCase.loadDocumentsForUser(getUserId())
            _notesArrangement.value =
                NotesArrangement.fromString(notesConfig.arrangementPref(getUserId()))
            _documentsState.value = ResultData.Complete(data)
        } catch (e: Exception) {
            _documentsState.value = ResultData.Error(e)
        }
    }
}
