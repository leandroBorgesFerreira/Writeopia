package io.storiesteller.auth.intronotes

import io.storiesteller.sdk.manager.DocumentRepository
import io.storiesteller.sdk.network.notes.NotesApi

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