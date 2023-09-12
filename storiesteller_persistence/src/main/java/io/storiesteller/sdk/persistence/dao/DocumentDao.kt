package io.storiesteller.sdk.persistence.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.storiesteller.sdk.persistence.entity.document.CREATED_AT
import io.storiesteller.sdk.persistence.entity.document.DOCUMENT_ENTITY
import io.storiesteller.sdk.persistence.entity.document.DocumentEntity
import io.storiesteller.sdk.persistence.entity.document.LAST_UPDATED_AT
import io.storiesteller.sdk.persistence.entity.document.TITLE
import io.storiesteller.sdk.persistence.entity.story.STORY_UNIT_ENTITY
import io.storiesteller.sdk.persistence.entity.story.StoryStepEntity

@Dao
interface DocumentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDocuments(vararg documents: DocumentEntity)

    @Delete
    suspend fun deleteDocuments(vararg documents: DocumentEntity)

    @Query("DELETE FROM $DOCUMENT_ENTITY WHERE user_id = :userId")
    suspend fun deleteDocumentsByUserId(userId: String)

    @Query("SELECT * FROM $DOCUMENT_ENTITY")
    suspend fun loadAllDocuments(): List<DocumentEntity>

    @Query("SELECT id FROM $DOCUMENT_ENTITY")
    suspend fun loadAllIds(): List<String>

    @Query("SELECT * FROM $DOCUMENT_ENTITY WHERE $DOCUMENT_ENTITY.id = :id")
    suspend fun loadDocumentById(id: String): DocumentEntity?

    /* The order here doesn't matter, because only one document should be returned */
    @Query(
        "SELECT * FROM $DOCUMENT_ENTITY " +
                "JOIN $STORY_UNIT_ENTITY ON $DOCUMENT_ENTITY.id = $STORY_UNIT_ENTITY.document_id " +
                "WHERE $DOCUMENT_ENTITY.id = :documentId " +
                "ORDER BY $DOCUMENT_ENTITY.created_at, $STORY_UNIT_ENTITY.position"
    )
    suspend fun loadDocumentWithContentById(
        documentId: String
    ): Map<DocumentEntity, List<StoryStepEntity>>?

    /* The order here doesn't matter, because only one document should be returned */
    @Query(
        "SELECT * FROM $DOCUMENT_ENTITY " +
                "JOIN $STORY_UNIT_ENTITY ON $DOCUMENT_ENTITY.id = $STORY_UNIT_ENTITY.document_id " +
                "WHERE $DOCUMENT_ENTITY.id IN (:documentIds) " +
                "ORDER BY " +
                "CASE WHEN :orderBy = \'$TITLE\' THEN $DOCUMENT_ENTITY.title END COLLATE NOCASE ASC, " +
                "CASE WHEN :orderBy = \'$CREATED_AT\' THEN $DOCUMENT_ENTITY.created_at END DESC, " +
                "CASE WHEN :orderBy = \'$LAST_UPDATED_AT\' THEN $DOCUMENT_ENTITY.last_updated_at END DESC, " +
                "$STORY_UNIT_ENTITY.position"
    )
    suspend fun loadDocumentWithContentByIds(
        documentIds: List<String>,
        orderBy: String
    ): Map<DocumentEntity, List<StoryStepEntity>>

    @Query(
        "SELECT * FROM $DOCUMENT_ENTITY " +
                "JOIN $STORY_UNIT_ENTITY ON $DOCUMENT_ENTITY.id = $STORY_UNIT_ENTITY.document_id " +
                "WHERE user_id = :userId " +
                "ORDER BY " +
                "CASE WHEN :orderBy = \'$TITLE\' THEN $DOCUMENT_ENTITY.title END COLLATE NOCASE ASC, " +
                "CASE WHEN :orderBy = \'$CREATED_AT\' THEN $DOCUMENT_ENTITY.created_at END DESC, " +
                "CASE WHEN :orderBy = \'$LAST_UPDATED_AT\' THEN $DOCUMENT_ENTITY.last_updated_at END DESC, " +
                "$STORY_UNIT_ENTITY.position"
    )
    suspend fun loadDocumentsWithContentForUser(
        orderBy: String,
        userId: String
    ): Map<DocumentEntity, List<StoryStepEntity>>?

    @Query("UPDATE $DOCUMENT_ENTITY set user_id = :newUserId WHERE user_id = :oldUserId")
    suspend fun moveDocumentsToNewUser(oldUserId: String, newUserId: String)
}
