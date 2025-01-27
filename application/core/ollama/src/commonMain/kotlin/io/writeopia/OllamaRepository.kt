package io.writeopia

import io.writeopia.api.OllamaApi
import kotlinx.coroutines.flow.Flow

class OllamaRepository(private val ollamaApi: OllamaApi) {

    suspend fun generateReply(model: String, prompt: String): String {
        return ollamaApi.generateReply(model, prompt).response
    }

    suspend fun streamReply(model: String, prompt: String): Flow<String> =
        ollamaApi.streamReply(model, prompt)
}
