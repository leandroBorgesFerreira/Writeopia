package br.com.leandroferreira.storyteller.persistence.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.leandroferreira.storyteller.persistence.entity.document.DOCUMENT_ENTITY
import br.com.leandroferreira.storyteller.persistence.entity.document.DocumentEntity
import br.com.leandroferreira.storyteller.persistence.entity.story.STORY_UNIT_ENTITY
import br.com.leandroferreira.storyteller.persistence.entity.story.StoryUnitEntity

@Dao
interface DocumentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDocuments(vararg documents: DocumentEntity)

    @Delete
    suspend fun deleteDocuments(vararg documents: DocumentEntity)

    @Query("SELECT * FROM $DOCUMENT_ENTITY")
    suspend fun loadAllDocuments(): List<DocumentEntity>

    @Query(
        "SELECT * FROM $DOCUMENT_ENTITY " +
            "JOIN $STORY_UNIT_ENTITY ON $DOCUMENT_ENTITY.id = $STORY_UNIT_ENTITY.document_id"
    )
    suspend fun loadDocumentWithContent(): Map<DocumentEntity, List<StoryUnitEntity>>
}
