package com.storiesteller.sdk.persistence.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.storiesteller.sdk.persistence.converter.InstantConverter
import com.storiesteller.sdk.persistence.converter.IdListConverter
import com.storiesteller.sdk.persistence.dao.DocumentDao
import com.storiesteller.sdk.persistence.dao.StoryUnitDao
import com.storiesteller.sdk.persistence.entity.document.DocumentEntity
import com.storiesteller.sdk.persistence.entity.story.StoryStepEntity

private const val DATABASE_NAME = "StoriesTellerDatabase"

@Database(
    entities = [
        DocumentEntity::class,
        StoryStepEntity::class,
    ],
    version = 3,
    exportSchema = false
)
@TypeConverters(IdListConverter::class, InstantConverter::class)
abstract class StoriesTellerDatabase : RoomDatabase() {

    abstract fun documentDao(): DocumentDao
    abstract fun storyUnitDao(): StoryUnitDao

    companion object {
        private var instance: StoriesTellerDatabase? = null

        fun database(
            context: Context,
            databaseName: String = DATABASE_NAME,
            inMemory: Boolean = false,
            builder: Builder<StoriesTellerDatabase>.() -> Builder<StoriesTellerDatabase> = { this }
        ): StoriesTellerDatabase =
            instance ?: createDatabase(context, databaseName, inMemory, builder)

        private fun createDatabase(
            context: Context,
            databaseName: String,
            inMemory: Boolean = false,
            builder: Builder<StoriesTellerDatabase>.() -> Builder<StoriesTellerDatabase> = { this }
        ): StoriesTellerDatabase =
            if (inMemory) {
                Room.inMemoryDatabaseBuilder(
                    context.applicationContext,
                    StoriesTellerDatabase::class.java
                ).build()
            } else {
                Room.databaseBuilder(
                    context.applicationContext,
                    StoriesTellerDatabase::class.java,
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
