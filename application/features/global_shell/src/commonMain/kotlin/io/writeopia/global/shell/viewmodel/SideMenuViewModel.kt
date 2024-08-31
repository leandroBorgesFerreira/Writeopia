package io.writeopia.global.shell.viewmodel

import io.writeopia.note_menu.ui.dto.MenuItemUi
import kotlinx.coroutines.flow.StateFlow

interface SideMenuViewModel{
    val sideMenuItems: StateFlow<List<MenuItemUi>>
    val showSideMenu: StateFlow<Boolean>
    val expandedFolders: StateFlow<Set<String>>
    val showSettingsState: StateFlow<Boolean>

    fun showSettings()
    fun hideSettings()
    fun moveToFolder(menuItemUi: MenuItemUi, parentId: String)
    fun expandFolder(id: String)
    fun highlightMenuItem()
    fun addFolder()
    fun editFolder(folder: MenuItemUi.FolderUi)
}
