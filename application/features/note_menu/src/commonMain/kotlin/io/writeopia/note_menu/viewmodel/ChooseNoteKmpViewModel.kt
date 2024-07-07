package io.writeopia.note_menu.viewmodel

import io.writeopia.auth.core.data.User
import io.writeopia.auth.core.manager.AuthManager
import io.writeopia.note_menu.data.model.Folder
import io.writeopia.note_menu.data.model.NotesArrangement
import io.writeopia.note_menu.data.model.NotesNavigation
import io.writeopia.note_menu.data.model.NotesNavigationType
import io.writeopia.note_menu.data.repository.ConfigurationRepository
import io.writeopia.note_menu.data.usecase.NotesUseCase
import io.writeopia.note_menu.extensions.toUiCard
import io.writeopia.note_menu.ui.dto.FolderEdit
import io.writeopia.note_menu.ui.dto.NotesUi
import io.writeopia.repository.UiConfigurationRepository
import io.writeopia.sdk.export.DocumentToJson
import io.writeopia.sdk.export.DocumentToMarkdown
import io.writeopia.sdk.export.DocumentWriter
import io.writeopia.sdk.import_document.json.WriteopiaJsonParser
import io.writeopia.sdk.models.document.Document
import io.writeopia.sdk.models.document.MenuItem
import io.writeopia.sdk.persistence.core.sorting.OrderBy
import io.writeopia.sdk.preview.PreviewParser
import io.writeopia.utils_module.DISCONNECTED_USER_ID
import io.writeopia.utils_module.KmpViewModel
import io.writeopia.utils_module.ResultData
import io.writeopia.utils_module.map
import io.writeopia.utils_module.toBoolean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

internal class ChooseNoteKmpViewModel(
    private val notesUseCase: NotesUseCase,
    private val notesConfig: ConfigurationRepository,
    private val uiConfigurationRepo: UiConfigurationRepository,
    private val authManager: AuthManager,
    private val selectionState: StateFlow<Boolean>,
    private val notesNavigation: NotesNavigation = NotesNavigation.Root,
    private val previewParser: PreviewParser = PreviewParser(),
    private val documentToMarkdown: DocumentToMarkdown = DocumentToMarkdown,
    private val documentToJson: DocumentToJson = DocumentToJson(),
    private val writeopiaJsonParser: WriteopiaJsonParser = WriteopiaJsonParser()
) : ChooseNoteViewModel, KmpViewModel() {

    private var localUserId: String? = null

    private val _selectedNotes = MutableStateFlow(setOf<String>())
    override val hasSelectedNotes: StateFlow<Boolean> by lazy {
        _selectedNotes.map { selectedIds ->
            selectedIds.isNotEmpty()
        }.stateIn(coroutineScope, SharingStarted.Lazily, false)
    }

    private val _documentsState: MutableStateFlow<ResultData<List<MenuItem>>> =
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

    private val _showSettingsState = MutableStateFlow(false)
    override val showSettingsState: StateFlow<Boolean> = _showSettingsState.asStateFlow()

    private val _editingFolderId = MutableStateFlow<String?>(null)

    override val editFolderState: StateFlow<FolderEdit?> by lazy {
        combine(_editingFolderId, folders) { id, folders ->
            if (id == null) null else folders[id]?.let { folder ->
                FolderEdit(folder.id, folder.title)
            }
        }.stateIn(coroutineScope, SharingStarted.Lazily, null)
    }

    override val folders: StateFlow<Map<String, Folder>> by lazy {
        notesUseCase.listenForFolders()
            .map { folders ->
                folders.associateBy { folder -> folder.id }
            }
            .stateIn(coroutineScope, SharingStarted.Lazily, emptyMap())
    }

    override val showSideMenu: StateFlow<Boolean> by lazy {
        uiConfigurationRepo.listenForUiConfiguration(::getUserId, coroutineScope)
            .map { configuration ->
                configuration?.showSideMenu ?: true
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
        _editingFolderId.value = null
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
        val selectedIds = _selectedNotes.value

        val allFavorites = (_documentsState.value as? ResultData.Complete<List<MenuItem>>)
            ?.data
            ?.filter { document ->
                selectedIds.contains(document.id)
            }
            ?.all { document -> document.favorite }
            ?: false

        coroutineScope.launch(Dispatchers.Default) {
            if (allFavorites) {
                notesUseCase.unFavoriteNotes(selectedIds)
            } else {
                notesUseCase.favoriteNotes(selectedIds)
            }

            refreshNotes()
        }
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
        val path = _showLocalSyncConfig.value.getPath()

        if (path != null) {
            coroutineScope.launch(Dispatchers.Default) {
                notesConfig.saveWorkspacePath(path = path, userId = getUserId())

                when (_showLocalSyncConfig.value.getSyncRequest()) {
                    SyncRequest.WRITE -> {
                        writeWorkspace(path)
                    }

                    SyncRequest.READ_WRITE -> {
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

    override fun showSettings() {
        _showSettingsState.value = true
    }

    override fun hideSettings() {
        _showSettingsState.value = false
    }

    override fun addFolder() {
        coroutineScope.launch(Dispatchers.Default) {
            notesUseCase.createFolder("Untitled", getUserId())
        }
    }

    override fun editFolder(id: String) {
        _editingFolderId.value = id
    }

    override fun updateFolder(folderEdit: FolderEdit) {
        folders.value[folderEdit.id]?.let { folder ->
            coroutineScope.launch(Dispatchers.Default) {
                notesUseCase.updateFolder(folder.copy(title = folderEdit.title))
            }
        }
    }

    override fun deleteFolder(id: String) {
        coroutineScope.launch(Dispatchers.Default) {
            notesUseCase.deleteFolderById(id)
            stopEditingFolder()
        }
    }

    override fun stopEditingFolder() {
        _editingFolderId.value = null
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
            val data = getNotes(userId)

            _notesArrangement.value =
                NotesArrangement.fromString(notesConfig.arrangementPref(userId))
            _documentsState.value = ResultData.Complete(data)
        } catch (e: Exception) {
            _documentsState.value = ResultData.Error(e)
        }
    }

    private suspend fun getNotes(userId: String): List<MenuItem> =
        if (notesNavigation.navigationType == NotesNavigationType.FAVORITES) {
            val orderBy = notesConfig.getOrderPreference(userId)
            notesUseCase.loadFavDocumentsForUser(orderBy, userId)
        } else {
            println("notesUseCase.loadContentForFolder")
            notesUseCase.loadContentForFolder(userId, Folder.ROOT_PATH)
        }

    private suspend fun getUserId(): String =
        localUserId ?: authManager.getUser().id.also { id ->
            localUserId = id
        }
}
