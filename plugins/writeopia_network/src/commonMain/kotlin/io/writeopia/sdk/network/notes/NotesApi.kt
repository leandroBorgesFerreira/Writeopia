package io.writeopia.sdk.network.notes

import io.writeopia.sdk.models.document.Document
import io.writeopia.sdk.serialization.data.DocumentApi
import io.writeopia.sdk.serialization.extensions.toModel
import io.writeopia.sdk.serialization.request.WriteopiaRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.writeopia.app.endpoints.EndPoints

/**
 * API for calling notes.
 */
class NotesApi(private val client: () -> HttpClient, private val baseUrl: String) {

    /**
     * The introductory notes of the app. The first notes after login.
     */
    suspend fun introNotes(): List<Document> {
        return client().get("${baseUrl}/${EndPoints.introNotes()}")
            .body<WriteopiaRequest<List<DocumentApi>>>()
            .data
            .map { documentApi -> documentApi.toModel() }
    }
}