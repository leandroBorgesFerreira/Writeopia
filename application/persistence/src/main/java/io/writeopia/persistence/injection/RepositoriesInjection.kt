package io.writeopia.persistence.injection

import android.app.Application
import io.writeopia.persistence.WriteopiaApplicationDatabase
import io.writeopia.sdk.manager.DocumentRepository
import io.writeopia.sdk.persistence.repository.DocumentRepositoryImpl

class RepositoriesInjection(application: Application) {

    private val database: WriteopiaApplicationDatabase = WriteopiaApplicationDatabase.database(application)

    fun provideDocumentRepository(): DocumentRepository =
        DocumentRepositoryImpl(
            database.documentDao(),
            database.storyUnitDao()
        )
}