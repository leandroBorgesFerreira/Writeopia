package io.writeopia.notemenu.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.writeopia.auth.core.data.User
import io.writeopia.auth.core.manager.AuthManager
import io.writeopia.common.utils.DISCONNECTED_USER_ID
import io.writeopia.common.utils.ResultData
import io.writeopia.common.utils.map
import io.writeopia.common.utils.toBoolean
import io.writeopia.commonui.extensions.toUiCard
import io.writeopia.models.Folder
import io.writeopia.notemenu.data.model.NotesArrangement
import io.writeopia.notemenu.data.model.NotesNavigation
import io.writeopia.notemenu.data.repository.ConfigurationRepository
import io.writeopia.notemenu.data.usecase.NotesUseCase
import io.writeopia.notemenu.ui.dto.NotesUi
import io.writeopia.onboarding.OnboardingState
import io.writeopia.sdk.export.DocumentToJson
import io.writeopia.sdk.export.DocumentToMarkdown
import io.writeopia.sdk.export.DocumentWriter
import io.writeopia.sdk.import.json.WriteopiaJsonParser
import io.writeopia.sdk.models.document.MenuItem
import io.writeopia.sdk.models.id.GenerateId
import io.writeopia.sdk.persistence.core.sorting.OrderBy
import io.writeopia.sdk.preview.PreviewParser
import io.writeopia.ui.keyboard.KeyboardEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

internal class ChooseNoteKmpViewModel(
    private val notesUseCase: NotesUseCase,
    private val notesConfig: ConfigurationRepository,
    private val authManager: AuthManager,
    private val selectionState: StateFlow<Boolean>,
    private val folderController: FolderStateController = FolderStateController(
        notesUseCase,
        authManager
    ),
    private val notesNavigation: NotesNavigation = NotesNavigation.Root,
    private val previewParser: PreviewParser = PreviewParser(),
    private val documentToMarkdown: DocumentToMarkdown = DocumentToMarkdown,
    private val documentToJson: DocumentToJson = DocumentToJson(),
    private val writeopiaJsonParser: WriteopiaJsonParser = WriteopiaJsonParser(),
    private val keyboardEventFlow: Flow<KeyboardEvent>,
) : ChooseNoteViewModel, ViewModel(), FolderController by folderController {

    init {
        folderController.initCoroutine(viewModelScope)

        viewModelScope.launch(Dispatchers.Default) {
            keyboardEventFlow.collect { event ->
                when (event) {
                    KeyboardEvent.DELETE -> {
                        requestPermissionToDeleteSelection()
                    }

                    KeyboardEvent.CANCEL -> {
                        clearSelection()
                    }

                    else -> {}
                }
            }
        }
    }

    private val _showOnboardingState =
        MutableStateFlow(OnboardingState.CONFIGURATION)
    override val showOnboardingState: StateFlow<OnboardingState> =
        _showOnboardingState.asStateFlow()

    private val _selectedNotes = MutableStateFlow(setOf<String>())
    override val hasSelectedNotes: StateFlow<Boolean> by lazy {
        _selectedNotes.map { selectedIds ->
            selectedIds.isNotEmpty()
        }.stateIn(viewModelScope, SharingStarted.Lazily, false)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val menuItemsPerFolderId: StateFlow<Map<String, List<MenuItem>>> by lazy {
        authManager.listenForUser()
            .flatMapLatest { user ->
                notesUseCase.listenForMenuItemsPerFolderId(notesNavigation, user.id)
            }.stateIn(viewModelScope, SharingStarted.Lazily, emptyMap())
    }

    override val menuItemsState: StateFlow<ResultData<List<MenuItem>>> by lazy {
        menuItemsPerFolderId.map { menuItems ->
            val pageItems = when (notesNavigation) {
                NotesNavigation.Favorites -> menuItems.values.flatten().filter { it.favorite }

                is NotesNavigation.Folder -> menuItems[notesNavigation.id]

                NotesNavigation.Root -> menuItems[Folder.ROOT_PATH]
            }

            ResultData.Complete(pageItems ?: emptyList())
        }.stateIn(viewModelScope, SharingStarted.Lazily, ResultData.Loading())
    }

    private val _user: MutableStateFlow<UserState<User>> = MutableStateFlow(UserState.Idle())
    override val userName: StateFlow<UserState<String>> by lazy {
        _user.map { userState ->
            userState.map { user ->
                user.name
            }
        }.stateIn(viewModelScope, SharingStarted.Lazily, UserState.Idle())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val notesArrangement: StateFlow<NotesArrangement> by lazy {
        authManager.listenForUser()
            .flatMapLatest { user ->
                notesConfig.listenForArrangementPref(user.id).map(NotesArrangement::fromString)
            }
            .stateIn(viewModelScope, SharingStarted.Lazily, NotesArrangement.GRID)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val orderByState: StateFlow<OrderBy> by lazy {
        authManager.listenForUser()
            .flatMapLatest { user ->
                notesConfig.listenOrderPreference(user.id).map(OrderBy::fromString)
            }
            .stateIn(viewModelScope, SharingStarted.Lazily, OrderBy.UPDATE)
    }

    private val _showLocalSyncConfig = MutableStateFlow<ConfigState>(ConfigState.Idle)
    override val showLocalSyncConfigState = _showLocalSyncConfig.asStateFlow()

    private val _editState = MutableStateFlow(false)
    override val editState: StateFlow<Boolean> = _editState.asStateFlow()

    private val _syncInProgress = MutableStateFlow<SyncState>(SyncState.Idle)
    override val syncInProgress = _syncInProgress.asStateFlow()

    private val _showSortMenuState = MutableStateFlow(false)
    override val showSortMenuState: StateFlow<Boolean> = _showSortMenuState.asStateFlow()

    private val _askToDelete = MutableStateFlow(false)

    override val titlesToDelete: StateFlow<List<String>> =
        combine(
            _askToDelete,
            _selectedNotes,
            menuItemsState
        ) { shouldAsk, selectedIds, itemsState ->
            if (shouldAsk && itemsState is ResultData.Complete) {
                val menuItem = itemsState.data

                menuItem.filter { item ->
                    selectedIds.contains(item.id)
                }.map { item ->
                    item.title
                }
            } else {
                emptyList()
            }
        }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    override val documentsState: StateFlow<ResultData<NotesUi>> by lazy {
        combine(
            _selectedNotes,
            menuItemsState,
            notesArrangement
        ) { selectedNoteIds, resultData, arrangement ->
            val previewLimit = when (arrangement) {
                NotesArrangement.LIST -> 4
                NotesArrangement.GRID -> 4
                NotesArrangement.STAGGERED_GRID -> 6
            }

            resultData.map { documentList ->
                NotesUi(
                    documentUiList = documentList.map { menuItem ->
                        menuItem.toUiCard(
                            previewParser = previewParser,
                            selected = selectedNoteIds.contains(menuItem.id),
                            limit = previewLimit,
                            expanded = false,
                        )
                    },
                    notesArrangement = arrangement
                )
            }
        }.stateIn(viewModelScope, SharingStarted.Lazily, ResultData.Idle())
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

    override fun handleMenuItemTap(id: String): Boolean {
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

    override fun toggleSelection(id: String) {
        if (_selectedNotes.value.contains(id)) {
            _selectedNotes.value -= id
        } else {
            _selectedNotes.value += id
        }
    }

    override fun showEditMenu() {
        _editState.value = true
    }

    override fun cancelEditMenu() {
        _editState.value = false
    }

    override fun onDocumentSelected(id: String, selected: Boolean) {
        viewModelScope.launch(Dispatchers.Default) {
            if (selected) {
                _selectedNotes.value += id
            } else {
                _selectedNotes.value -= id
            }
        }
    }

    override fun clearSelection() {
        _selectedNotes.value = emptySet()
    }

    override fun listArrangementSelected() {
        viewModelScope.launch {
            notesConfig.saveDocumentArrangementPref(NotesArrangement.LIST, getUserId())
        }
    }

    override fun gridArrangementSelected() {
        viewModelScope.launch {
            notesConfig.saveDocumentArrangementPref(NotesArrangement.GRID, getUserId())
        }
    }

    override fun staggeredGridArrangementSelected() {
        viewModelScope.launch {
            notesConfig.saveDocumentArrangementPref(NotesArrangement.STAGGERED_GRID, getUserId())
        }
    }

    override fun sortingSelected(orderBy: OrderBy) {
        viewModelScope.launch {
            notesConfig.saveDocumentSortingPref(orderBy, getUserId())
        }
    }

    override fun copySelectedNotes() {
        viewModelScope.launch(Dispatchers.Default) {
            notesUseCase.duplicateDocuments(_selectedNotes.value.toList(), getUserId())
        }
    }

    override fun deleteSelectedNotes() {
        val selected = _selectedNotes.value

        viewModelScope.launch(Dispatchers.Default) {
            notesUseCase.deleteNotes(selected)
            clearSelection()
            _askToDelete.value = false
        }
    }

    override fun favoriteSelectedNotes() {
        val selectedIds = _selectedNotes.value

        val allFavorites = (menuItemsState.value as? ResultData.Complete<List<MenuItem>>)
            ?.data
            ?.filter { document ->
                selectedIds.contains(document.id)
            }
            ?.all { document -> document.favorite }
            ?: false

        viewModelScope.launch(Dispatchers.Default) {
            if (allFavorites) {
                notesUseCase.unFavoriteDocuments(selectedIds)
            } else {
                notesUseCase.favoriteDocuments(selectedIds)
            }
        }
    }

    override fun unSelectNotes() {
        _selectedNotes.value = emptySet()
    }

    override fun showSortMenu() {
        _showSortMenuState.value = true
    }

    override fun cancelSortMenu() {
        _showSortMenuState.value = false
    }

    override fun configureDirectory() {
        viewModelScope.launch(Dispatchers.Default) {
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
        val now = Clock.System.now()

        viewModelScope.launch(Dispatchers.Default) {
            writeopiaJsonParser.readDocuments(filePaths)
                .onCompletion { exception ->
                    if (exception == null) {
//                        refreshNotes()
                        cancelEditMenu()
                    }
                }
                .map { document ->
                    document.copy(
                        parentId = notesNavigation.id,
                        id = GenerateId.generate(),
                        lastUpdatedAt = now,
                        createdAt = now,
                        userId = getUserId(),
                        favorite = false
                    )
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
            viewModelScope.launch(Dispatchers.Default) {
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

    override fun requestPermissionToDeleteSelection() {
        _askToDelete.value = true
    }

    override fun cancelDeletion() {
        _askToDelete.value = false
    }

    override fun showOnboarding() {
        _showOnboardingState.value = OnboardingState.CONFIGURATION
    }

    override fun hideOnboarding() {
        _showOnboardingState.value = OnboardingState.HIDDEN
    }

    override fun completeOnboarding() {
        viewModelScope.launch {
            _showOnboardingState.value = OnboardingState.CONGRATULATION
            delay(3000)
            _showOnboardingState.value = OnboardingState.COMPLETE
        }
    }

    private fun handleStorage(workspaceFunc: suspend (String) -> Unit, syncRequest: SyncRequest) {
        viewModelScope.launch(Dispatchers.Default) {
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
            path = path,
            usePath = true
        )

        writeopiaJsonParser.readAllWorkSpace(path)
            .onCompletion {
                delay(150)
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
            path = path,
            usePath = true
        )

        delay(150)
        _syncInProgress.value = SyncState.Idle
    }

    private fun directoryFilesAs(path: String, documentWriter: DocumentWriter) {
        viewModelScope.launch(Dispatchers.Default) {
            val data = notesUseCase.loadDocumentsForUser(getUserId())
            documentWriter.writeDocuments(data, path, usePath = true)
        }
    }

    private suspend fun getUserId(): String = authManager.getUser().id
}

