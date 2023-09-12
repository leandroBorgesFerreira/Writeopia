package com.storiesteller.sdk.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.storiesteller.sdk.persistence.entity.story.STORY_UNIT_ENTITY
import com.storiesteller.sdk.persistence.entity.story.StoryStepEntity

@Dao
interface StoryUnitDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStoryUnits(vararg storyStep: StoryStepEntity)

    @Update
    suspend fun updateStoryStep(storyStep: StoryStepEntity)

    @Query("DELETE FROM $STORY_UNIT_ENTITY WHERE $STORY_UNIT_ENTITY.document_id = :documentId")
    suspend fun deleteDocumentContent(documentId: String)

    @Query("SELECT * FROM $STORY_UNIT_ENTITY WHERE $STORY_UNIT_ENTITY.document_id = :documentId " +
            "ORDER BY position" )
    suspend fun loadDocumentContent(documentId: String): List<StoryStepEntity>

    @Query("SELECT * FROM $STORY_UNIT_ENTITY WHERE $STORY_UNIT_ENTITY.id = :storyId" )
    suspend fun queryById(storyId: String): StoryStepEntity?

    @Query("SELECT * FROM $STORY_UNIT_ENTITY WHERE $STORY_UNIT_ENTITY.parent_id = :parentId " +
            "ORDER BY position")
    suspend fun queryInnerSteps(parentId: String): List<StoryStepEntity>
}
