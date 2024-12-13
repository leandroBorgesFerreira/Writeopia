package io.writeopia.features.search.extensions

import io.writeopia.app.sql.FolderEntity
import io.writeopia.common.utils.extensions.toBoolean
import io.writeopia.notemenu.data.model.Folder
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
    )
