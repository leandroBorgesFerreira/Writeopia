package io.writeopia.persistence.injection

import io.writeopia.persistence.WriteopiaApplicationDatabase
import io.writeopia.sdk.persistence.core.dao.DocumentRepository
import io.writeopia.sdk.persistence.core.di.DaosInjector
import io.writeopia.sdk.persistence.dao.room.RoomDocumentRepository

class RoomDaosInjection(
    private val database: WriteopiaApplicationDatabase
) : DaosInjector {

    override fun provideDocumentDao(): DocumentRepository =
        RoomDocumentRepository(
            database.documentDao(),
            database.storyUnitDao()
        )
}