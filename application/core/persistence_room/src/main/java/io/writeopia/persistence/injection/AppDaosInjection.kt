package io.writeopia.persistence.injection

import io.writeopia.persistence.WriteopiaApplicationDatabase
import io.writeopia.persistence.data.daos.RoomNotesConfigurationDao

class AppDaosInjection(private val database: WriteopiaApplicationDatabase) {

    fun provideConfigurationDao(): RoomNotesConfigurationDao = database.notesConfigurationDao()
}