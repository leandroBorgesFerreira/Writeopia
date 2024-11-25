package io.writeopia.sdk.persistence.entity.document

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import io.writeopia.sdk.persistence.core.*
import kotlinx.datetime.Clock

@Entity(tableName = DOCUMENT_ENTITY)
data class DocumentEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(TITLE) val title: String,
    @ColumnInfo(CREATED_AT) val createdAt: Long,
    @ColumnInfo(LAST_UPDATED_AT) val lastUpdatedAt: Long,
    @ColumnInfo(USER_ID) val userId: String,
    @ColumnInfo(FAVORITE) val favorite: Boolean,
    @ColumnInfo(PARENT_ID) val parentId: String,
) {
    companion object {
        fun createById(id: String, userId: String, parentId: String): DocumentEntity {
            val now = Clock.System.now().toEpochMilliseconds()
            return DocumentEntity(id, "", now, now, userId, favorite = false, parentId = parentId)
        }
    }
}
