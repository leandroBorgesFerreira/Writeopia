package io.writeopia.persistence.injection

import io.writeopia.persistence.WriteopiaApplicationDatabase
import io.writeopia.persistence.data.daos.NotesConfigurationDao

class AppDaosInjection(private val database: WriteopiaApplicationDatabase) {

    fun provideConfigurationDao(): NotesConfigurationDao = database.notesConfigurationDao()
}