package io.writeopia.notemenu.viewmodel

import io.writeopia.common.utils.IconChange
import io.writeopia.commonui.dtos.MenuItemUi
import io.writeopia.models.Folder

interface FolderController {
    fun addFolder()

    fun editFolder(folder: MenuItemUi.FolderUi)

    fun updateFolder(folderEdit: Folder)

    fun deleteFolder(id: String)

    fun stopEditingFolder()

    fun moveToFolder(menuItemUi: MenuItemUi, parentId: String)

    fun changeIcons(menuItemId: String, icon: String, tint: Int, iconChange: IconChange)
}
