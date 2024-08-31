package io.writeopia.global.shell.viewmodel

import io.writeopia.auth.core.manager.AuthManager
import io.writeopia.note_menu.data.usecase.NotesUseCase
import io.writeopia.note_menu.ui.dto.MenuItemUi
import io.writeopia.repository.UiConfigurationRepository
import io.writeopia.utils_module.KmpViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SideMenuKmpViewModel(
    private val notesUseCase: NotesUseCase,
    private val uiConfigurationRepo: UiConfigurationRepository,
    private val authManager: AuthManager,
): SideMenuViewModel, KmpViewModel() {

    private var localUserId: String? = null
    private val _editingFolder = MutableStateFlow<MenuItemUi.FolderUi?>(null)

    private val _expandedFolders = MutableStateFlow(setOf<String>())
    override val expandedFolders: StateFlow<Set<String>> = _expandedFolders.asStateFlow()

    private val _showSettingsState = MutableStateFlow(false)
    override val showSettingsState: StateFlow<Boolean> = _showSettingsState.asStateFlow()

    override val showSideMenu: StateFlow<Boolean> by lazy {
        uiConfigurationRepo.listenForUiConfiguration(::getUserId, coroutineScope)
            .map { configuration ->
                configuration?.showSideMenu ?: true
            }.stateIn(coroutineScope, SharingStarted.Lazily, false)
    }

    override fun addFolder() {
        coroutineScope.launch(Dispatchers.Default) {
            notesUseCase.createFolder("Untitled", getUserId())
        }
    }

    override fun editFolder(folder: MenuItemUi.FolderUi) {
        _editingFolder.value = folder
    }

    override val sideMenuItems: StateFlow<List<MenuItemUi>> by lazy {
        combine(_expandedFolders, menuItemsPerFolderId, highlightItem) { expanded, folderMap, highlighted ->
        }
    }

    override fun showSettings() {
        _showSettingsState.value = true
    }

    override fun hideSettings() {
        _showSettingsState.value = false
    }

    override fun moveToFolder(menuItemUi: MenuItemUi, parentId: String) {
        if (menuItemUi.documentId != parentId) {
            coroutineScope.launch(Dispatchers.Default) {
                notesUseCase.moveItem(menuItemUi, parentId)
            }
        }
    }

    override fun expandFolder(id: String) {
        val expanded = _expandedFolders.value
        if (expanded.contains(id)) {
            coroutineScope.launch(Dispatchers.Default) {
                _expandedFolders.value = expanded - id
            }
        } else {
            notesUseCase.listenForMenuItemsByParentId(id, ::getUserId, coroutineScope)
            _expandedFolders.value = expanded + id
        }
    }

    override fun highlightMenuItem() {
//        _highlightItem.value = notesNavigation.id
    }

    private suspend fun getUserId(): String =
        localUserId ?: authManager.getUser().id.also { id ->
            localUserId = id
        }
}
