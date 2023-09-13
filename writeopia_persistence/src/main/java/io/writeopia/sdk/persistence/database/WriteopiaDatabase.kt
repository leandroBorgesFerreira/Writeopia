package io.writeopia.sdk.persistence.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.writeopia.sdk.persistence.converter.InstantConverter
import io.writeopia.sdk.persistence.converter.IdListConverter
import io.writeopia.sdk.persistence.dao.DocumentDao
import io.writeopia.sdk.persistence.dao.StoryUnitDao
import io.writeopia.sdk.persistence.entity.document.DocumentEntity
import io.writeopia.sdk.persistence.entity.story.StoryStepEntity

private const val DATABASE_NAME = "WriteopiaDatabase"

@Database(
    entities = [
        DocumentEntity::class,
        StoryStepEntity::class,
    ],
    version = 3,
    exportSchema = false
)
@TypeConverters(IdListConverter::class, InstantConverter::class)
abstract class WriteopiaDatabase : RoomDatabase() {

    abstract fun documentDao(): DocumentDao
    abstract fun storyUnitDao(): StoryUnitDao

    companion object {
        private var instance: WriteopiaDatabase? = null

        fun database(
            context: Context,
            databaseName: String = DATABASE_NAME,
            inMemory: Boolean = false,
            builder: Builder<WriteopiaDatabase>.() -> Builder<WriteopiaDatabase> = { this }
        ): WriteopiaDatabase =
            instance ?: createDatabase(context, databaseName, inMemory, builder)

        private fun createDatabase(
            context: Context,
            databaseName: String,
            inMemory: Boolean = false,
            builder: Builder<WriteopiaDatabase>.() -> Builder<WriteopiaDatabase> = { this }
        ): WriteopiaDatabase =
            if (inMemory) {
                Room.inMemoryDatabaseBuilder(
                    context.applicationContext,
                    WriteopiaDatabase::class.java
                ).build()
            } else {
                Room.databaseBuilder(
                    context.applicationContext,
                    WriteopiaDatabase::class.java,
                    databaseName
                ).let(builder)
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { database ->
                        instance = database
                    }
            }
    }
}
