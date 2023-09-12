package com.storiesteller.auth.intronotes

import com.storiesteller.sdk.manager.DocumentRepository
import com.storiesteller.sdk.network.notes.NotesApi

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