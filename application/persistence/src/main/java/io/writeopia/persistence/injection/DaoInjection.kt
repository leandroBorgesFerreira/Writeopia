package io.writeopia.persistence.injection

import android.app.Application
import io.writeopia.persistence.WriteopiaApplicationDatabase
import io.writeopia.sdk.manager.DocumentDao
import io.writeopia.sdk.persistence.repository.DocumentDao

class DaoInjection(application: Application) {

    private val database: WriteopiaApplicationDatabase = WriteopiaApplicationDatabase.database(application)

    fun provideDocumentDao(): io.writeopia.sdk.manager.DocumentDao =
        DocumentDao(
            database.documentDao(),
            database.storyUnitDao()
        )
}