package com.github.leandroborgesferreira.storyteller.persistence.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.github.leandroborgesferreira.storyteller.persistence.converter.InstantConverter
import com.github.leandroborgesferreira.storyteller.persistence.converter.IdListConverter
import com.github.leandroborgesferreira.storyteller.persistence.dao.DocumentDao
import com.github.leandroborgesferreira.storyteller.persistence.dao.StoryUnitDao
import com.github.leandroborgesferreira.storyteller.persistence.entity.document.DocumentEntity
import com.github.leandroborgesferreira.storyteller.persistence.entity.story.StoryStepEntity

private const val DATABASE_NAME = "StoryTellerDatabase"

@Database(
    entities = [
        DocumentEntity::class,
        StoryStepEntity::class,
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(IdListConverter::class, InstantConverter::class)
abstract class StoryTellerDatabase : RoomDatabase() {

    abstract fun documentDao(): DocumentDao
    abstract fun storyUnitDao(): StoryUnitDao

    companion object {
        private var instance: StoryTellerDatabase? = null

        fun database(
            context: Context,
            databaseName: String = DATABASE_NAME,
            inMemory: Boolean = false
        ): StoryTellerDatabase = instance ?: createDatabase(context, databaseName, inMemory)

        private fun createDatabase(
            context: Context,
            databaseName: String,
            inMemory: Boolean = false
        ): StoryTellerDatabase =
            if (inMemory) {
                Room.inMemoryDatabaseBuilder(
                    context.applicationContext,
                    StoryTellerDatabase::class.java
                ).build()
            } else {
                Room.databaseBuilder(
                    context.applicationContext,
                    StoryTellerDatabase::class.java,
                    databaseName
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { database ->
                        instance = database
                    }
            }
    }
}
