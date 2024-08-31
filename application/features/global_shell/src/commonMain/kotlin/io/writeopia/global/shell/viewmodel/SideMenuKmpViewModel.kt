package io.writeopia.global.shell.viewmodel

import io.writeopia.note_menu.ui.dto.MenuItemUi
import io.writeopia.utils_module.KmpViewModel
import kotlinx.coroutines.flow.StateFlow

class SideMenuKmpViewModel: SideMenuViewModel, KmpViewModel() {

    override val sideMenuItems: StateFlow<List<MenuItemUi>>
        get() = TODO("Not yet implemented")

    override fun showSettings() {
        TODO("Not yet implemented")
    }

    override fun hideSettings() {
        TODO("Not yet implemented")
    }

    override fun moveToFolder(menuItemUi: MenuItemUi, parentId: String) {
        TODO("Not yet implemented")
    }

    override fun expandFolder(id: String) {
        TODO("Not yet implemented")
    }

    override fun highlightMenuItem() {
        TODO("Not yet implemented")
    }
}
