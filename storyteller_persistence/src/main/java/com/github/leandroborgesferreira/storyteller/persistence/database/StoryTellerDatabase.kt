package com.github.leandroborgesferreira.storyteller.persistence.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.github.leandroborgesferreira.storyteller.persistence.converter.DateConverter
import com.github.leandroborgesferreira.storyteller.persistence.converter.IdListConverter
import com.github.leandroborgesferreira.storyteller.persistence.dao.DocumentDao
import com.github.leandroborgesferreira.storyteller.persistence.dao.StoryUnitDao
import com.github.leandroborgesferreira.storyteller.persistence.entity.document.DocumentEntity
import com.github.leandroborgesferreira.storyteller.persistence.entity.story.StoryUnitEntity

private const val DATABASE_NAME = "StoryTellerDatabase"

@Database(
    entities = [
        DocumentEntity::class,
        StoryUnitEntity::class,
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(IdListConverter::class, DateConverter::class)
abstract class StoryTellerDatabase : RoomDatabase() {

    abstract fun documentDao(): DocumentDao
    abstract fun storyUnitDao(): StoryUnitDao

    companion object {
        private var instance: StoryTellerDatabase? = null

        fun database(context: Context, databaseName: String = DATABASE_NAME): StoryTellerDatabase {
            return instance
                ?: Room.databaseBuilder(
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
