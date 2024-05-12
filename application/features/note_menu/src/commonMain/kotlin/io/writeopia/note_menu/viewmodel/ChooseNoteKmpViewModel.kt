package io.writeopia.note_menu.viewmodel

import io.writeopia.auth.core.data.User
import io.writeopia.auth.core.manager.AuthManager
import io.writeopia.note_menu.data.NotesArrangement
import io.writeopia.note_menu.data.repository.ConfigurationRepository
import io.writeopia.note_menu.data.repository.UiConfigurationRepository
import io.writeopia.note_menu.data.usecase.NotesUseCase
import io.writeopia.note_menu.extensions.toUiCard
import io.writeopia.note_menu.ui.dto.NotesUi
import io.writeopia.sdk.export.DocumentToJson
import io.writeopia.sdk.export.DocumentToMarkdown
import io.writeopia.sdk.export.DocumentWriter
import io.writeopia.sdk.import_document.json.WriteopiaJsonParser
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
    private val notesConfig: ConfigurationRepository,
    private val uiConfigurationRepo: UiConfigurationRepository,
    private val authManager: AuthManager,
    private val selectionState: StateFlow<Boolean>,
    private val previewParser: PreviewParser = PreviewParser(),
    private val documentToMarkdown: DocumentToMarkdown = DocumentToMarkdown,
    private val documentToJson: DocumentToJson = DocumentToJson(),
    private val writeopiaJsonParser: WriteopiaJsonParser = WriteopiaJsonParser()
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

    private val _showLocalSyncConfig = MutableStateFlow<ConfigState>(ConfigState.Idle)
    override val showLocalSyncConfigState = _showLocalSyncConfig.asStateFlow()

    private val _editState = MutableStateFlow(false)
    override val editState: StateFlow<Boolean> = _editState.asStateFlow()

    private val _syncInProgress = MutableStateFlow<SyncState>(SyncState.Idle)
    override val syncInProgress = _syncInProgress.asStateFlow()

    private val _showSortMenuState = MutableStateFlow(false)
    override val showSortMenuState: StateFlow<Boolean> = _showSortMenuState.asStateFlow()

    override val showSideMenu: StateFlow<Boolean> by lazy {
        uiConfigurationRepo.listenForColorTheme(::getUserId, coroutineScope).map { configuration ->
            configuration.showSideMenu
        }.stateIn(coroutineScope, SharingStarted.Lazily, false)
    }

    override val documentsState: StateFlow<ResultData<NotesUi>> by lazy {
        combine(
            _selectedNotes,
            _documentsState,
            notesArrangement
        ) { selectedNoteIds, resultData, arrangement ->
            val previewLimit = when (arrangement) {
                NotesArrangement.LIST -> 4
                NotesArrangement.GRID -> 4
                NotesArrangement.STAGGERED_GRID -> 10
            }

            resultData.map { documentList ->
                NotesUi(
                    documentUiList = documentList.map { document ->
                        document.toUiCard(
                            previewParser,
                            selectedNoteIds.contains(document.id),
                            previewLimit
                        )
                    },
                    notesArrangement = arrangement
                )

            }
        }.stateIn(coroutineScope, SharingStarted.Lazily, ResultData.Idle())
    }

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

    override fun handleNoteTap(id: String): Boolean {
        return if (selectionState.value) {
            if (_selectedNotes.value.contains(id)) {
                _selectedNotes.value -= id
            } else {
                _selectedNotes.value += id
            }

            true
        } else {
            false
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

    override fun staggeredGridArrangementSelected() {
        coroutineScope.launch {
            notesConfig.saveDocumentArrangementPref(NotesArrangement.STAGGERED_GRID, getUserId())
            _notesArrangement.value = NotesArrangement.STAGGERED_GRID
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
        //Todo: Implement!
    }

    override fun showSortMenu() {
        _showSortMenuState.value = true
    }

    override fun cancelSortMenu() {
        _showSortMenuState.value = false
    }

    override fun configureDirectory() {
        coroutineScope.launch(Dispatchers.Default) {
            _showLocalSyncConfig.value =
                ConfigState.Configure(
                    path = notesConfig.loadWorkspacePath(getUserId()) ?: "",
                    syncRequest = SyncRequest.CONFIGURE
                )
        }
    }

    override fun directoryFilesAsMarkdown(path: String) {
        directoryFilesAs(path, documentToMarkdown)
        cancelEditMenu()
    }

    override fun loadFiles(filePaths: List<String>) {
        coroutineScope.launch(Dispatchers.Default) {
            writeopiaJsonParser.readDocuments(filePaths)
                .onCompletion { exception ->
                    if (exception == null) {
                        refreshNotes()
                        cancelEditMenu()
                    }
                }
                .collect(notesUseCase::saveDocument)
        }
    }

    override fun hideConfigSyncMenu() {
        _showLocalSyncConfig.value = ConfigState.Idle
    }

    override fun confirmWorkplacePath() {
        println("confirmWorkplacePath")

        val path = _showLocalSyncConfig.value.getPath()

        if (path != null) {
            println("path != null")
            coroutineScope.launch(Dispatchers.Default) {
                println("launch")
                notesConfig.saveWorkspacePath(path = path, userId = getUserId())

                when (_showLocalSyncConfig.value.getSyncRequest()) {
                    SyncRequest.WRITE -> {
                        println("writeWorkspace")
                        writeWorkspace(path)
                    }

                    SyncRequest.READ_WRITE -> {
                        println("syncWorkplace")
                        syncWorkplace(path)
                    }

                    SyncRequest.CONFIGURE, null -> {}
                }

                _showLocalSyncConfig.value = ConfigState.Idle
            }
        }
    }

    override fun pathSelected(path: String) {
        _showLocalSyncConfig.value = _showLocalSyncConfig.value.setPath { path }
    }

    override fun onSyncLocallySelected() {
        handleStorage(::syncWorkplace, SyncRequest.READ_WRITE)
    }

    override fun onWriteLocallySelected() {
        handleStorage(::writeWorkspace, SyncRequest.WRITE)
    }

    override fun toggleSideMenu() {
        setShowSideMenu(!showSideMenu.value)
    }

    private fun setShowSideMenu(enabled: Boolean) {
        coroutineScope.launch(Dispatchers.Default) {
            uiConfigurationRepo.updateShowSideMenu(userId = getUserId(), showSideMenu = enabled)
        }
    }

    private fun handleStorage(workspaceFunc: suspend (String) -> Unit, syncRequest: SyncRequest) {
        coroutineScope.launch(Dispatchers.Default) {
            val userId = getUserId()
            val workspacePath = notesConfig.loadWorkspacePath(userId)

            if (workspacePath != null) {
                workspaceFunc(workspacePath)
            } else {
                _showLocalSyncConfig.value = ConfigState.Configure("", syncRequest)
            }
        }
    }

    private suspend fun syncWorkplace(path: String) {
        _syncInProgress.value = SyncState.LoadingSync

        val userId = getUserId()
        val currentNotes = writeopiaJsonParser.lastUpdatesById(path)?.let { lastUpdated ->
            notesUseCase.loadDocumentsForUserAfterTime(userId, lastUpdated)
        } ?: notesUseCase.loadDocumentsForUser(userId)

        documentToJson.writeDocuments(
            documents = currentNotes,
            path = path
        )

        writeopiaJsonParser.readAllWorkSpace(path)
            .onCompletion {
                refreshNotes()
                _syncInProgress.value = SyncState.Idle
            }
            .collect(notesUseCase::saveDocument)
    }

    private suspend fun writeWorkspace(path: String) {
        _syncInProgress.value = SyncState.LoadingWrite

        val userId = getUserId()
        val currentNotes = writeopiaJsonParser.lastUpdatesById(path)?.let { lastUpdated ->
            notesUseCase.loadDocumentsForUserAfterTime(userId, lastUpdated)
        } ?: run {
            notesUseCase.loadDocumentsForUser(userId)
        }

        documentToJson.writeDocuments(
            documents = currentNotes,
            path = path
        )

        _syncInProgress.value = SyncState.Idle
    }

    private fun directoryFilesAs(path: String, documentWriter: DocumentWriter) {
        coroutineScope.launch(Dispatchers.Default) {
            val data = notesUseCase.loadDocumentsForUser(getUserId())
            documentWriter.writeDocuments(data, path)
        }
    }

    private suspend fun refreshNotes() {
        _documentsState.value = ResultData.Loading()

        try {
            val userId = getUserId()

            val data = notesUseCase.loadDocumentsForUser(userId)
            _notesArrangement.value =
                NotesArrangement.fromString(notesConfig.arrangementPref(userId))
            _documentsState.value = ResultData.Complete(data)
        } catch (e: Exception) {
            _documentsState.value = ResultData.Error(e)
        }
    }
}
