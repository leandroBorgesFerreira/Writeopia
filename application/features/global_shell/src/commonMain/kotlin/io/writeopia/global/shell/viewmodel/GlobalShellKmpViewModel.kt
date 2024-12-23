package io.writeopia.global.shell.viewmodel

import androidx.compose.ui.unit.dp
import io.writeopia.auth.core.manager.AuthManager
import io.writeopia.common.utils.IconChange
import io.writeopia.common.utils.KmpViewModel
import io.writeopia.common.utils.collections.reverseTraverse
import io.writeopia.common.utils.collections.toNodeTree
import io.writeopia.model.ColorThemeOption
import io.writeopia.model.UiConfiguration
import io.writeopia.models.Folder
import io.writeopia.notemenu.data.model.NotesNavigation
import io.writeopia.notemenu.data.usecase.NotesNavigationUseCase
import io.writeopia.notemenu.data.usecase.NotesUseCase
import io.writeopia.notemenu.extensions.toUiCard
import io.writeopia.notemenu.ui.dto.MenuItemUi
import io.writeopia.notemenu.viewmodel.FolderController
import io.writeopia.notemenu.viewmodel.FolderStateController
import io.writeopia.repository.UiConfigurationRepository
import io.writeopia.sdk.models.document.MenuItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
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
    private var sideMenuWidthState = MutableStateFlow<Float?>(null)

    override fun initCoroutine(coroutineScope: CoroutineScope) {
        super.initCoroutine(coroutineScope)
        folderStateController.initCoroutine(coroutineScope)
    }

    private val _showSettingsState = MutableStateFlow(false)
    override val showSettingsState: StateFlow<Boolean> = _showSettingsState.asStateFlow()

    private val _expandedFolders = MutableStateFlow(setOf<String>())

    private val _showSearch = MutableStateFlow(false)
    override val showSearchDialog: StateFlow<Boolean> = _showSearch.asStateFlow()

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

    override val showSideMenuState: StateFlow<Float> by lazy {
        combine(
            uiConfigurationRepo.listenForUiConfiguration(::getUserId, coroutineScope)
                .filterNotNull(),
            sideMenuWidthState.asStateFlow()
        ) { configuration, width ->
            width ?: configuration.sideMenuWidth
        }.stateIn(coroutineScope, SharingStarted.Lazily, 280F)
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
        val width = showSideMenuState.value

        sideMenuWidthState.value = if (width.dp < 5.dp) 280F else 0F
        saveMenuWidth()
    }

    override fun saveMenuWidth() {
        val width = sideMenuWidthState.value ?: 280F

        coroutineScope.launch(Dispatchers.Default) {
            val uiConfiguration =
                uiConfigurationRepo.getUiConfigurationEntity("disconnected_user")
                    ?: UiConfiguration(
                        userId = "disconnected_user",
                        colorThemeOption = ColorThemeOption.SYSTEM,
                        sideMenuWidth = width
                    )
            uiConfigurationRepo.insertUiConfiguration(uiConfiguration.copy(sideMenuWidth = width))
        }
    }

    override fun moveSideMenu(width: Float) {
        sideMenuWidthState.value = width
    }

    override fun showSettings() {
        _showSettingsState.value = true
    }

    override fun hideSettings() {
        _showSettingsState.value = false
    }

    override fun showSearch() {
        _showSearch.value = true
    }

    override fun hideSearch() {
        _showSearch.value = false
    }

    override fun changeIcons(menuItemId: String, icon: String, tint: Int, iconChange: IconChange) {
        coroutineScope.launch {
            when (iconChange) {
                IconChange.FOLDER -> notesUseCase.updateFolderById(menuItemId) { folder ->
                    folder.copy(icon = MenuItem.Icon(icon, tint))
                }

                IconChange.DOCUMENT -> notesUseCase.updateDocumentById(menuItemId) { document ->
                    document.copy(icon = MenuItem.Icon(icon, tint))
                }
            }
        }
    }

    private suspend fun getUserId(): String =
        localUserId ?: authManager.getUser().id.also { id ->
            localUserId = id
        }
}
