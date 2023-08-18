package com.github.leandroborgesferreira.storyteller.network.notes

import com.github.leandroborgesferreira.storyteller.serialization.data.StoryStepApi
import com.github.leandroborgesferreira.storyteller.serialization.request.StoryTellerRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class NotesApi(private val client: HttpClient, private val baseUrl: String) {

    suspend fun introNotes(): StoryTellerRequest<List<StoryStepApi>> {
        val response = client.get("${baseUrl}/intro/notes")
        return response.body()
    }
}