package io.writeopia.persistence.injection

import io.writeopia.persistence.WriteopiaApplicationDatabase
import io.writeopia.sdk.persistence.core.dao.DocumentDao
import io.writeopia.sdk.persistence.dao.room.RoomDocumentDao

class RepositoriesInjection(
    private val database: WriteopiaApplicationDatabase
) {

    fun provideDocumentRepository(): DocumentDao =
        RoomDocumentDao(
            database.documentDao(),
            database.storyUnitDao()
        )
}