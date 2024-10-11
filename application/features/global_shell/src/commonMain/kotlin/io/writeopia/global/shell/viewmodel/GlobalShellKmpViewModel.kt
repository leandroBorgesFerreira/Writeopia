package io.writeopia.global.shell.viewmodel

import io.writeopia.auth.core.manager.AuthManager
import io.writeopia.note_menu.data.model.Folder
import io.writeopia.note_menu.data.model.NotesNavigation
import io.writeopia.note_menu.data.usecase.NotesNavigationUseCase
import io.writeopia.note_menu.data.usecase.NotesUseCase
import io.writeopia.note_menu.extensions.toUiCard
import io.writeopia.note_menu.ui.dto.MenuItemUi
import io.writeopia.note_menu.viewmodel.FolderController
import io.writeopia.note_menu.viewmodel.FolderStateController
import io.writeopia.repository.UiConfigurationRepository
import io.writeopia.sdk.models.document.MenuItem
import io.writeopia.common.utils.KmpViewModel
import io.writeopia.common.utils.collections.reverseTraverse
import io.writeopia.common.utils.collections.toNodeTree
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class GlobalShellKmpViewModel(
    private val notesUseCase: NotesUseCase,
    private val uiConfigurationRepo: UiConfigurationRepository,
    private val authManager: AuthManager,
    private val notesNavigationUseCase: NotesNavigationUseCase,
    private val folderStateController: FolderStateController = FolderStateController(
        notesUseCase,
        authManager
    ),
) : GlobalShellViewModel, KmpViewModel(), FolderController by folderStateController {

    private var localUserId: String? = null

    override fun initCoroutine(coroutineScope: CoroutineScope) {
        super.initCoroutine(coroutineScope)
        folderStateController.initCoroutine(coroutineScope)
    }

    private val _showSettingsState = MutableStateFlow(false)
    override val showSettingsState: StateFlow<Boolean> = _showSettingsState.asStateFlow()

    private val _expandedFolders = MutableStateFlow(setOf<String>())

    override val highlightItem: StateFlow<String?> by lazy {
        notesNavigationUseCase.navigationState
            .map { navigation -> navigation.id }
            .stateIn(coroutineScope, SharingStarted.Lazily, NotesNavigation.Root.id)
    }

    override val editFolderState: StateFlow<Folder?> by lazy {
        combine(
            folderStateController.editingFolderState,
            menuItemsPerFolderId
        ) { selectedFolder, menuItems ->
            if (selectedFolder != null) {
                val folder = menuItems[selectedFolder.parentId]?.find { menuItem ->
                    menuItem.id == selectedFolder.id
                } as? Folder

                folder
            } else {
                null
            }
        }.stateIn(coroutineScope, SharingStarted.Lazily, null)
    }

    override val showSideMenu: StateFlow<Boolean> by lazy {
        uiConfigurationRepo.listenForUiConfiguration(::getUserId, coroutineScope)
            .map { configuration ->
                configuration?.showSideMenu ?: true
            }.stateIn(coroutineScope, SharingStarted.Lazily, false)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val menuItemsPerFolderId: StateFlow<Map<String, List<MenuItem>>> by lazy {
        combine(
            authManager.listenForUser(),
            notesNavigationUseCase.navigationState
        ) { user, notesNavigation ->
            user to notesNavigation
        }.flatMapLatest { (user, notesNavigation) ->
            notesUseCase.listenForMenuItemsPerFolderId(notesNavigation, user.id, coroutineScope)
        }.stateIn(coroutineScope, SharingStarted.Lazily, emptyMap())
    }

    override val sideMenuItems: StateFlow<List<MenuItemUi>> by lazy {
        combine(
            _expandedFolders,
            menuItemsPerFolderId,
            highlightItem
        ) { expanded, folderMap, highlighted ->
            val folderUiMap = folderMap.mapValues { (_, item) ->
                item.map {
                    it.toUiCard(
                        expanded = expanded.contains(it.id),
                        highlighted = it.id == highlighted
                    )
                }
            }

            val itemsList = folderUiMap
                .toNodeTree(
                    MenuItemUi.FolderUi.root(),
                    filterPredicate = { menuItemUi ->
                        expanded.contains(menuItemUi.documentId)
                    }
                )
                .toList() as List<MenuItemUi>

            itemsList.toMutableList().apply {
                removeAt(0)
            }
        }.stateIn(coroutineScope, SharingStarted.Lazily, emptyList())
    }

    override val folderPath: StateFlow<List<String>> by lazy {
        combine(
            menuItemsPerFolderId,
            notesNavigationUseCase.navigationState
        ) { perFolder, navigation ->
            val menuItems = perFolder.values.flatten().map { it.toUiCard() }
            listOf("Home") + menuItems.reverseTraverse(
                navigation.id,
                filterPredicate = { item -> item is MenuItemUi.FolderUi },
                mapFunc = { item -> item.title }
            )
        }.stateIn(coroutineScope, SharingStarted.Lazily, emptyList())
    }

    override fun expandFolder(id: String) {
        val expanded = _expandedFolders.value
        if (expanded.contains(id)) {
            coroutineScope.launch(Dispatchers.Default) {
                _expandedFolders.value = expanded - id
            }
        } else {
            coroutineScope.launch {
                notesUseCase.listenForMenuItemsByParentId(id, getUserId(), coroutineScope)
                _expandedFolders.value = expanded + id
            }
        }
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

    private suspend fun getUserId(): String =
        localUserId ?: authManager.getUser().id.also { id ->
            localUserId = id
        }

    private fun setShowSideMenu(enabled: Boolean) {
        coroutineScope.launch(Dispatchers.Default) {
            uiConfigurationRepo.updateShowSideMenu(userId = getUserId(), showSideMenu = enabled)
        }
    }
}
