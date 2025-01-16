package io.writeopia.auth.intronotes

import io.writeopia.sdk.network.notes.NotesApi
import io.writeopia.sdk.persistence.core.repository.DocumentRepository

internal class IntroNotesUseCase(
    private val documentRepository: DocumentRepository,
    private val notesApi: NotesApi,
) {
    suspend fun addIntroNotes(userId: String) {
        notesApi.introNotes()
            .map { document -> document.copy(userId = userId) }
            .forEach { document ->
                documentRepository.saveDocument(document)
            }
    }
}
