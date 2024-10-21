package io.writeopia.note_menu.viewmodel

import io.writeopia.note_menu.data.model.Folder
import io.writeopia.note_menu.ui.dto.MenuItemUi

interface FolderController {
    fun addFolder()

    fun editFolder(folder: MenuItemUi.FolderUi)

    fun updateFolder(folderEdit: Folder)

    fun deleteFolder(id: String)

    fun stopEditingFolder()

    fun moveToFolder(menuItemUi: MenuItemUi, parentId: String)
}
