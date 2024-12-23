package io.writeopia.notemenu.viewmodel

import io.writeopia.common.utils.IconChange
import io.writeopia.models.Folder
import io.writeopia.notemenu.ui.dto.MenuItemUi

interface FolderController {
    fun addFolder()

    fun editFolder(folder: MenuItemUi.FolderUi)

    fun updateFolder(folderEdit: Folder)

    fun deleteFolder(id: String)

    fun stopEditingFolder()

    fun moveToFolder(menuItemUi: MenuItemUi, parentId: String)

    fun changeIcons(menuItemId: String, icon: String, iconChange: IconChange)
}
