package br.com.leandroferreira.storyteller.persistence.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.com.leandroferreira.storyteller.persistence.converter.IdListConverter
import br.com.leandroferreira.storyteller.persistence.dao.DocumentDao
import br.com.leandroferreira.storyteller.persistence.dao.StoryUnitDao
import br.com.leandroferreira.storyteller.persistence.entity.document.DocumentEntity
import br.com.leandroferreira.storyteller.persistence.entity.story.StoryUnitEntity

private const val DATABASE_NAME = "StoryTellerDatabase"

@Database(
    entities = [
        DocumentEntity::class,
        StoryUnitEntity::class,
    ],
    version = 3,
    exportSchema = false
)
@TypeConverters(IdListConverter::class)
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
