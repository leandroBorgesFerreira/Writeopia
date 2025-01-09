package io.writeopia.notemenu.extensions

import io.writeopia.app.sql.FolderEntity
import io.writeopia.common.utils.extensions.toBoolean
import io.writeopia.common.utils.extensions.toLong
import io.writeopia.models.Folder
import io.writeopia.sdk.models.document.MenuItem
import kotlinx.datetime.Instant

fun Folder.toEntity() = FolderEntity(
    id = id,
    parent_id = parentId,
    user_id = userId,
    title = title,
    created_at = createdAt.toEpochMilliseconds(),
    last_updated_at = lastUpdatedAt.toEpochMilliseconds(),
    favorite = favorite.toLong(),
    icon = icon?.label,
    icon_tint = icon?.tint?.toLong()
)
