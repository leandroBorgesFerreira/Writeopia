package io.writeopia.persistence.room.injection

import io.writeopia.persistence.room.WriteopiaApplicationDatabase
import io.writeopia.persistence.room.data.daos.NotesConfigurationRoomDao

class AppRoomDaosInjection(private val database: WriteopiaApplicationDatabase) {

    fun provideConfigurationDao(): NotesConfigurationRoomDao = database.notesConfigurationDao()
}