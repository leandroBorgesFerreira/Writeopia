package io.writeopia.global.shell.viewmodel

import io.writeopia.note_menu.ui.dto.MenuItemUi
import kotlinx.coroutines.flow.StateFlow

interface SideMenuViewModel {
    val sideMenuItems: StateFlow<List<MenuItemUi>>

    fun showSettings()
    fun hideSettings()
    fun moveToFolder(menuItemUi: MenuItemUi, parentId: String)
    fun expandFolder(id: String)
    fun highlightMenuItem()
}
