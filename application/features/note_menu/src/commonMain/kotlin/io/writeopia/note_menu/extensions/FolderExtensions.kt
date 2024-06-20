package io.writeopia.note_menu.extensions

import io.writeopia.app.sql.FolderEntity
import io.writeopia.extensions.toBoolean
import io.writeopia.note_menu.data.model.Folder
import kotlinx.datetime.Instant

fun FolderEntity.toModel() =
    Folder(
        id = this.id,
        parentId = this.parent_id,
        title = title,
        createdAt = Instant.fromEpochMilliseconds(created_at),
        lastUpdatedAt = Instant.fromEpochMilliseconds(last_updated_at),
        userId = user_id,
        favorite = favorite.toBoolean(),
    )
