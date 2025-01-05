package io.writeopia.persistence.room

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

private const val DATABASE_NAME = "WriteopiaDatabase"

object DatabaseConfig {
    fun roomBuilder(context: Context): RoomDatabase.Builder<WriteopiaApplicationDatabase> =
        Room.databaseBuilder(
            context.applicationContext,
            WriteopiaApplicationDatabase::class.java,
            DATABASE_NAME
        )

    fun roomInMemory(context: Context): RoomDatabase.Builder<WriteopiaApplicationDatabase> =
        Room.inMemoryDatabaseBuilder(
            context.applicationContext,
            WriteopiaApplicationDatabase::class.java
        )
}
