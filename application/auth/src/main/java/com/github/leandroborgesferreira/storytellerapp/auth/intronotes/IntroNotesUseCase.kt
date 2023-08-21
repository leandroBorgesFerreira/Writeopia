package com.github.leandroborgesferreira.storytellerapp.auth.intronotes

import com.github.leandroborgesferreira.storyteller.manager.DocumentRepository
import com.github.leandroborgesferreira.storyteller.network.notes.NotesApi

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