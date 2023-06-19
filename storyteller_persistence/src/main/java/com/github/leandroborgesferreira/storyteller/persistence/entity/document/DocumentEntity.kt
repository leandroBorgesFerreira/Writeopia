package com.github.leandroborgesferreira.storyteller.persistence.entity.document

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

internal const val DOCUMENT_ENTITY: String = "DOCUMENT_ENTITY_TABLE"

@Entity(tableName = DOCUMENT_ENTITY)
data class DocumentEntity(
    @PrimaryKey val id: String,
    @ColumnInfo("title") val title: String,
    @ColumnInfo("created_at") val createdAt: Date,
    @ColumnInfo("last_updated_at") val lastUpdatedAt: Date,
)
