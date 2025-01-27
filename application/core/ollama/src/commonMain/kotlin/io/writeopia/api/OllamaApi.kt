package io.writeopia.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.writeopia.app.endpoints.EndPoints
import io.writeopia.model.OllamaResponse
import io.writeopia.requests.OllamaGenerateRequest

/**
 * API for calling Ollama
 */
class OllamaApi(
    private val client: HttpClient,
    private val baseUrl: String = "http://localhost:11434/api"
) {

    suspend fun generateReply(
        model: String,
        prompt: String,
        stream: Boolean = false
    ): OllamaResponse =
        client.post("$baseUrl/${EndPoints.ollamaGenerate()}") {
            contentType(ContentType.Application.Json)
            setBody(OllamaGenerateRequest(model, prompt, stream))
        }.body<OllamaResponse>()
}
