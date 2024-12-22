package io.writeopia.notemenu.extensions

import io.writeopia.app.sql.FolderEntity
import io.writeopia.common.utils.extensions.toBoolean
import io.writeopia.common.utils.extensions.toLong
import io.writeopia.models.Folder
import kotlinx.datetime.Instant

fun FolderEntity.toModel(count: Long) =
    Folder(
        id = this.id,
        parentId = this.parent_id,
        title = title,
        createdAt = Instant.fromEpochMilliseconds(created_at),
        lastUpdatedAt = Instant.fromEpochMilliseconds(last_updated_at),
        userId = user_id,
        itemCount = count,
        favorite = favorite.toBoolean(),
        icon = icon
    )

fun Folder.toEntity() = FolderEntity(
    id = id,
    parent_id = parentId,
    user_id = userId,
    title = title,
    created_at = createdAt.toEpochMilliseconds(),
    last_updated_at = lastUpdatedAt.toEpochMilliseconds(),
    favorite = favorite.toLong(),
    icon = icon
)
