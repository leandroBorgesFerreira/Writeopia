package io.writeopia.auth.intronotes

import io.writeopia.sdk.persistence.core.dao.DocumentDao
import io.writeopia.sdk.network.notes.NotesApi

internal class IntroNotesUseCase(
    private val documentDao: DocumentDao,
    private val notesApi: NotesApi
) {

    suspend fun addIntroNotes() {
        notesApi.introNotes().forEach { document ->
            documentDao.saveDocument(document)
        }
    }
}