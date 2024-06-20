package io.writeopia.note_menu.extensions

import io.writeopia.app.sql.FolderEntity
import io.writeopia.note_menu.data.model.Folder

fun FolderEntity.toModel() =
    Folder(
        id = this.id,
        parentId = this.parent_id
    )
