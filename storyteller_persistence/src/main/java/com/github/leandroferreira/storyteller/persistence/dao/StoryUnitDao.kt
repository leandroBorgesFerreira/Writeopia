package com.github.leandroferreira.storyteller.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.leandroferreira.storyteller.persistence.entity.story.STORY_UNIT_ENTITY
import com.github.leandroferreira.storyteller.persistence.entity.story.StoryUnitEntity

@Dao
interface StoryUnitDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStoryUnits(vararg documents: StoryUnitEntity)

    @Query("DELETE FROM $STORY_UNIT_ENTITY WHERE $STORY_UNIT_ENTITY.document_id = :documentId")
    suspend fun deleteDocumentContent(documentId: String)

    @Query("SELECT * FROM $STORY_UNIT_ENTITY WHERE $STORY_UNIT_ENTITY.document_id = :documentId" )
    suspend fun loadDocumentContent(documentId: String): List<StoryUnitEntity>

    @Query("SELECT * FROM $STORY_UNIT_ENTITY WHERE $STORY_UNIT_ENTITY.id = :storyId" )
    suspend fun queryById(storyId: String): StoryUnitEntity?

    @Query("SELECT * FROM $STORY_UNIT_ENTITY WHERE $STORY_UNIT_ENTITY.parent_id = :parentId" )
    suspend fun queryInnerSteps(parentId: String): List<StoryUnitEntity>
}
