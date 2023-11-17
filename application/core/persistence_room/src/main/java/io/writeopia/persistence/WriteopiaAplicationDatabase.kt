package io.writeopia.persistence

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.writeopia.persistence.data.daos.RoomNotesConfigurationDao
import io.writeopia.persistence.data.entities.NotesConfigurationEntity
import io.writeopia.sdk.persistence.converter.IdListConverter
import io.writeopia.sdk.persistence.dao.DocumentEntityDao
import io.writeopia.sdk.persistence.dao.StoryUnitEntityDao
import io.writeopia.sdk.persistence.entity.document.DocumentEntity
import io.writeopia.sdk.persistence.entity.story.StoryStepEntity

private const val DATABASE_NAME = "WriteopiaDatabase"

@Database(
    entities = [
        DocumentEntity::class,
        StoryStepEntity::class,
        NotesConfigurationEntity::class
    ],
    version = 5,
    exportSchema = false
)
@TypeConverters(IdListConverter::class)
abstract class WriteopiaApplicationDatabase : RoomDatabase() {

    abstract fun documentDao(): DocumentEntityDao
    abstract fun storyUnitDao(): StoryUnitEntityDao
    abstract fun notesConfigurationDao(): RoomNotesConfigurationDao

    companion object {
        private var instance: WriteopiaApplicationDatabase? = null

        fun database(
            context: Context,
            databaseName: String = DATABASE_NAME,
            inMemory: Boolean = false,
        ): WriteopiaApplicationDatabase =
            instance ?: createDatabase(context, databaseName, inMemory)

        private fun createDatabase(
            context: Context,
            databaseName: String,
            inMemory: Boolean = false,
        ): WriteopiaApplicationDatabase =
            if (inMemory) {
                Room.inMemoryDatabaseBuilder(
                    context.applicationContext,
                    WriteopiaApplicationDatabase::class.java
                ).build()
            } else {
                Room.databaseBuilder(
                    context.applicationContext,
                    WriteopiaApplicationDatabase::class.java,
                    databaseName
                )
                    .createFromAsset("WriteopiaDatabase.db")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { database ->
                        instance = database
                    }
            }
    }
}
