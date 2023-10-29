package io.writeopia.persistence.injection

import android.app.Application
import io.writeopia.persistence.WriteopiaApplicationDatabase
import io.writeopia.sdk.manager.DocumentRepository
import io.writeopia.sdk.persistence.repository.DocumentRepositoryImpl

class RepositoriesInjection(
    private val database: WriteopiaApplicationDatabase
) {

    fun provideDocumentRepository(): DocumentRepository =
        DocumentRepositoryImpl(
            database.documentDao(),
            database.storyUnitDao()
        )
}