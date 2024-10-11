package io.writeopia.persistence.room.injection

import io.writeopia.persistence.room.WriteopiaApplicationDatabase
import io.writeopia.sdk.persistence.core.di.RepositoryInjector
import io.writeopia.sdk.persistence.core.repository.DocumentRepository
import io.writeopia.sdk.persistence.dao.room.RoomDocumentRepository

class RoomRespositoryInjection(
    private val database: WriteopiaApplicationDatabase
) : RepositoryInjector {

    override fun provideDocumentRepository(): DocumentRepository =
        RoomDocumentRepository(
            database.documentDao(),
            database.storyUnitDao()
        )
}