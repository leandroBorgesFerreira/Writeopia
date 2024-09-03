package io.writeopia.global.shell.viewmodel

import io.writeopia.note_menu.data.model.Folder
import io.writeopia.note_menu.ui.dto.MenuItemUi
import io.writeopia.note_menu.viewmodel.FolderController
import io.writeopia.sdk.models.document.MenuItem
import kotlinx.coroutines.flow.StateFlow

interface GlobalShellViewModel : FolderController {
    val sideMenuItems: StateFlow<List<MenuItemUi>>
    val showSideMenu: StateFlow<Boolean>
    val highlightItem: StateFlow<String?>
    val menuItemsPerFolderId: StateFlow<Map<String, List<MenuItem>>>
    val editFolderState : StateFlow<Folder?>
    val showSettingsState: StateFlow<Boolean>
    val folderPath: StateFlow<List<String>>

    fun expandFolder(id: String)
    fun toggleSideMenu()
    fun showSettings()
    fun hideSettings()
}
