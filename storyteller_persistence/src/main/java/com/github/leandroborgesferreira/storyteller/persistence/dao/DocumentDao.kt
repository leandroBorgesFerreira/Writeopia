package com.github.leandroborgesferreira.storyteller.persistence.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.leandroborgesferreira.storyteller.persistence.entity.document.CREATED_AT
import com.github.leandroborgesferreira.storyteller.persistence.entity.document.DOCUMENT_ENTITY
import com.github.leandroborgesferreira.storyteller.persistence.entity.document.DocumentEntity
import com.github.leandroborgesferreira.storyteller.persistence.entity.document.LAST_UPDATED_AT
import com.github.leandroborgesferreira.storyteller.persistence.entity.document.TITLE
import com.github.leandroborgesferreira.storyteller.persistence.entity.story.STORY_UNIT_ENTITY
import com.github.leandroborgesferreira.storyteller.persistence.entity.story.StoryUnitEntity
import com.github.leandroborgesferreira.storyteller.persistence.sorting.OrderBy

@Dao
interface DocumentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDocuments(vararg documents: DocumentEntity)

    @Delete
    suspend fun deleteDocuments(vararg documents: DocumentEntity)

    @Query("SELECT * FROM $DOCUMENT_ENTITY")
    suspend fun loadAllDocuments(): List<DocumentEntity>

    @Query("SELECT id FROM $DOCUMENT_ENTITY")
    suspend fun loadAllIds(): List<String>

    @Query("SELECT * FROM $DOCUMENT_ENTITY WHERE $DOCUMENT_ENTITY.id = :id")
    suspend fun loadDocumentById(id: String): DocumentEntity

    /* The order here doesn't matter, because only one document should be returned */
    @Query(
        "SELECT * FROM $DOCUMENT_ENTITY " +
                "JOIN $STORY_UNIT_ENTITY ON $DOCUMENT_ENTITY.id = $STORY_UNIT_ENTITY.document_id " +
                "WHERE $DOCUMENT_ENTITY.id = :documentId " +
                "ORDER BY $DOCUMENT_ENTITY.created_at, $STORY_UNIT_ENTITY.position"
    )
    suspend fun loadDocumentWithContentById(
        documentId: String
    ): Map<DocumentEntity, List<StoryUnitEntity>>?

    /* The order here doesn't matter, because only one document should be returned */
    @Query(
        "SELECT * FROM $DOCUMENT_ENTITY " +
                "JOIN $STORY_UNIT_ENTITY ON $DOCUMENT_ENTITY.id = $STORY_UNIT_ENTITY.document_id " +
                "WHERE $DOCUMENT_ENTITY.id IN (:documentIds) " +
                "ORDER BY " +
                "CASE WHEN :orderBy = \'$TITLE\' THEN $DOCUMENT_ENTITY.title END ASC, " +
                "CASE WHEN :orderBy = \'$CREATED_AT\' THEN $DOCUMENT_ENTITY.created_at END DESC, " +
                "CASE WHEN :orderBy = \'$LAST_UPDATED_AT\' THEN $DOCUMENT_ENTITY.last_updated_at END DESC"
    )
    suspend fun loadDocumentWithContentByIds(
        documentIds: List<String>,
        orderBy: String
    ): Map<DocumentEntity, List<StoryUnitEntity>>

    @Query(
        "SELECT * FROM $DOCUMENT_ENTITY " +
                "JOIN $STORY_UNIT_ENTITY ON $DOCUMENT_ENTITY.id = $STORY_UNIT_ENTITY.document_id " +
                "ORDER BY " +
                "CASE WHEN :orderBy = \'$TITLE\' THEN $DOCUMENT_ENTITY.title END ASC, " +
                "CASE WHEN :orderBy = \'$CREATED_AT\' THEN $DOCUMENT_ENTITY.created_at END DESC, " +
                "CASE WHEN :orderBy = \'$LAST_UPDATED_AT\' THEN $DOCUMENT_ENTITY.last_updated_at END DESC, " +
                "$STORY_UNIT_ENTITY.position"
    )
    suspend fun loadDocumentWithContent(orderBy: String): Map<DocumentEntity, List<StoryUnitEntity>>?
}
