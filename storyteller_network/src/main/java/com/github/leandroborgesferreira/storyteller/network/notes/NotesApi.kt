package com.github.leandroborgesferreira.storyteller.network.notes

import com.github.leandroborgesferreira.storyteller.models.document.Document
import com.github.leandroborgesferreira.storyteller.serialization.data.DocumentApi
import com.github.leandroborgesferreira.storyteller.serialization.extensions.toModel
import com.github.leandroborgesferreira.storyteller.serialization.request.StoryTellerRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

/**
 * API for calling notes.
 */
class NotesApi(private val client: () -> HttpClient, private val baseUrl: String) {

    /**
     * The introductory notes of the app. The first notes after login.
     */
    suspend fun introNotes(): List<Document> {
        return client().get("${baseUrl}/notes/intro")
            .body<StoryTellerRequest<List<DocumentApi>>>()
            .data
            .map { documentApi -> documentApi.toModel() }
    }
}