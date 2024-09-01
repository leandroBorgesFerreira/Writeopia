package io.writeopia.global.shell.viewmodel

import io.writeopia.note_menu.ui.dto.MenuItemUi
import io.writeopia.sdk.models.document.MenuItem
import kotlinx.coroutines.flow.StateFlow

interface SideMenuViewModel{
    val sideMenuItems: StateFlow<List<MenuItemUi>>
    val showSideMenu: StateFlow<Boolean>
    val expandedFolders: StateFlow<Set<String>>
    val showSettingsState: StateFlow<Boolean>
    val highlightItem: StateFlow<String?>
    val menuItemsPerFolderId: StateFlow<Map<String, List<MenuItem>>>

    fun showSettings()
    fun hideSettings()
    fun moveToFolder(menuItemUi: MenuItemUi, parentId: String)
    fun expandFolder(id: String)
    fun addFolder()
    fun editFolder(folder: MenuItemUi.FolderUi)
}
