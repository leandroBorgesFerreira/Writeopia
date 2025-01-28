package io.writeopia.global.shell.viewmodel

import io.writeopia.common.utils.ResultData
import io.writeopia.commonui.dtos.MenuItemUi
import io.writeopia.models.Folder
import io.writeopia.notemenu.viewmodel.FolderController
import io.writeopia.sdk.models.document.MenuItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface GlobalShellViewModel : FolderController {
    val sideMenuItems: StateFlow<List<MenuItemUi>>

    val showSideMenuState: StateFlow<Float>

    val highlightItem: StateFlow<String?>

    val menuItemsPerFolderId: StateFlow<Map<String, List<MenuItem>>>

    val editFolderState: StateFlow<Folder?>

    val showSettingsState: StateFlow<Boolean>

    val folderPath: StateFlow<List<String>>

    val showSearchDialog: StateFlow<Boolean>

    val workspaceLocalPath: StateFlow<String>

    fun init()

    fun expandFolder(id: String)

    fun toggleSideMenu()

    fun showSettings()

    fun hideSettings()

    fun saveMenuWidth()

    fun moveSideMenu(width: Float)

    fun showSearch()

    fun hideSearch()

    fun changeWorkspaceLocalPath(path: String)

    fun getModels(): Flow<ResultData<List<String>>>
}
