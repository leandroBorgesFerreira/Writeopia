package br.com.leandroferreira.storyteller.persistence.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.leandroferreira.storyteller.persistence.entity.story.STORY_UNIT_ENTITY
import br.com.leandroferreira.storyteller.persistence.entity.story.StoryUnitEntity

@Dao
interface StoryUnitDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStoryUnits(vararg documents: StoryUnitEntity)

    @Query("DELETE FROM $STORY_UNIT_ENTITY WHERE $STORY_UNIT_ENTITY.document_id = :documentId")
    suspend fun deleteDocumentContent(documentId: String)

    @Query("SELECT * FROM $STORY_UNIT_ENTITY")
    suspend fun loadDocumentContent(): List<StoryUnitEntity>
}
