package io.writeopia.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.preparePost
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.readUTF8Line
import io.writeopia.app.endpoints.EndPoints
import io.writeopia.requests.Model
import io.writeopia.requests.ModelsResponse
import io.writeopia.requests.OllamaGenerateRequest
import io.writeopia.responses.OllamaResponse
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import kotlinx.serialization.json.Json

/**
 * API for calling Ollama
 */
class OllamaApi(
    private val client: HttpClient,
    private val baseUrl: String = "http://localhost:11434/api",
    private val json: Json
) {

    suspend fun generateReply(
        model: String,
        prompt: String,
    ): OllamaResponse =
        client.post("$baseUrl/${EndPoints.ollamaGenerate()}") {
            contentType(ContentType.Application.Json)
            setBody(OllamaGenerateRequest(model, prompt, false))
        }.body<OllamaResponse>()

    fun streamReply(
        model: String,
        prompt: String,
    ): Flow<String> =
        flow {
            client.preparePost {
                url("$baseUrl/${EndPoints.ollamaGenerate()}")
                contentType(ContentType.Application.Json)
                setBody(OllamaGenerateRequest(model, prompt, true))
            }.execute { response ->
                val stringBuilder = StringBuilder()
                val channel = response.body<ByteReadChannel>()

                while (currentCoroutineContext().isActive && !channel.isClosedForRead) {
                    val line = channel.readUTF8Line()?.takeUnless { it.isEmpty() } ?: continue

                    val value: OllamaResponse = json.decodeFromString(line)

                    stringBuilder.append(" ${value.response}")

                    emit(stringBuilder.toString())
                }
            }
        }

    fun getModels(): Flow<ModelsResponse> {
        return flow {
            val request = client.get("$baseUrl/${EndPoints.ollamaModels()}") {
                contentType(ContentType.Application.Json)
            }

            try {
                emit(request.body())
            } catch (e: Exception) {
                println("Error: ${e.message}")
                emit(ModelsResponse(emptyList()))
            }
        }
    }
}

