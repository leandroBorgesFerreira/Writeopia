package io.writeopia.note_menu.extensions

import io.writeopia.note_menu.data.model.Folder
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
        itemCount = itemCount, // Assuming itemCount is not stored in the entity
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
