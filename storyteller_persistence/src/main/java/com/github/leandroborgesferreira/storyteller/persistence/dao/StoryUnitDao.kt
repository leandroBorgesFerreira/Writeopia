package com.github.leandroborgesferreira.storyteller.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.github.leandroborgesferreira.storyteller.model.story.StoryStep
import com.github.leandroborgesferreira.storyteller.persistence.entity.story.STORY_UNIT_ENTITY
import com.github.leandroborgesferreira.storyteller.persistence.entity.story.StoryStepEntity

@Dao
interface StoryUnitDao {

    @Upsert
    suspend fun insertStoryUnits(vararg storyStep: StoryStepEntity)

    @Update
    suspend fun updateStoryStep(storyStep: StoryStepEntity)

    @Query("DELETE FROM $STORY_UNIT_ENTITY WHERE $STORY_UNIT_ENTITY.document_id = :documentId")
    suspend fun deleteDocumentContent(documentId: String)

    @Query("SELECT * FROM $STORY_UNIT_ENTITY WHERE $STORY_UNIT_ENTITY.document_id = :documentId" )
    suspend fun loadDocumentContent(documentId: String): List<StoryStepEntity>

    @Query("SELECT * FROM $STORY_UNIT_ENTITY WHERE $STORY_UNIT_ENTITY.id = :storyId" )
    suspend fun queryById(storyId: String): StoryStepEntity?

    @Query("SELECT * FROM $STORY_UNIT_ENTITY WHERE $STORY_UNIT_ENTITY.parent_id = :parentId" )
    suspend fun queryInnerSteps(parentId: String): List<StoryStepEntity>
}
