package br.com.leandroferreira.storyteller.persistence.entity.document

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

internal const val DOCUMENT_ENTITY: String = "DOCUMENT_ENTITY_TABLE"

@Entity(tableName = DOCUMENT_ENTITY)
data class DocumentEntity(
    @PrimaryKey() val id: String,
    @ColumnInfo("title") val title: String,
)
