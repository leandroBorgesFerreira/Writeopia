package io.writeopia.auth.intronotes

import io.writeopia.sdk.persistence.core.repository.DocumentRepository
import io.writeopia.sdk.network.notes.NotesApi

internal class IntroNotesUseCase(
    private val documentRepository: DocumentRepository,
    private val notesApi: NotesApi
) {

    suspend fun addIntroNotes() {
        notesApi.introNotes().forEach { document ->
            documentRepository.saveDocument(document)
        }
    }
}