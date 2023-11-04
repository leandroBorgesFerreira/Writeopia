package io.writeopia.persistence.injection

import io.writeopia.persistence.WriteopiaApplicationDatabase
import io.writeopia.sdk.persistence.core.dao.DocumentDao
import io.writeopia.sdk.persistence.dao.room.RoomDocumentDao

class RoomDaosInjection(
    private val database: WriteopiaApplicationDatabase
) {

    fun provideDocumentDao(): DocumentDao =
        RoomDocumentDao(
            database.documentDao(),
            database.storyUnitDao()
        )
}