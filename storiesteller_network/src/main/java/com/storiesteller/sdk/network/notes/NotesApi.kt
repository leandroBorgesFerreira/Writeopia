package com.storiesteller.sdk.network.notes

import com.storiesteller.sdk.models.document.Document
import com.storiesteller.sdk.serialization.data.DocumentApi
import com.storiesteller.sdk.serialization.extensions.toModel
import com.storiesteller.sdk.serialization.request.StoriesTellerRequest
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
            .body<StoriesTellerRequest<List<DocumentApi>>>()
            .data
            .map { documentApi -> documentApi.toModel() }
    }
}