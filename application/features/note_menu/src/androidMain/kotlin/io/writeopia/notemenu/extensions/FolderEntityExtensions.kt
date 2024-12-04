package io.writeopia.notemenu.extensions

import io.writeopia.notemenu.data.model.Folder
import io.writeopia.persistence.room.data.entities.FolderEntity
import kotlinx.datetime.Instant

fun FolderEntity.toModel(itemCount: Long): Folder {
    return Folder(
        id = id,
        parentId = parentId,
        title = title,
        createdAt = Instant.fromEpochMilliseconds(createdAt),
        lastUpdatedAt = Instant.fromEpochMilliseconds(lastUpdatedAt),
        userId = userId,
        favorite = favorite,
        // Assuming itemCount is not stored in the entity
        itemCount = itemCount,
    )
}

fun Folder.toRoomEntity(): FolderEntity {
    return FolderEntity(
        id = id,
        parentId = parentId,
        title = title,
        createdAt = createdAt.toEpochMilliseconds(),
        lastUpdatedAt = lastUpdatedAt.toEpochMilliseconds(),
        userId = userId,
        favorite = favorite
    )
}
