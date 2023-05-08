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
    suspend fun insertDocuments(vararg documents: StoryUnitEntity)

    @Delete
    suspend fun deleteUsers(vararg documents: StoryUnitEntity)

    @Query("SELECT * FROM $STORY_UNIT_ENTITY")
    suspend fun loadAllStoryUnits(): List<StoryUnitEntity>
}
