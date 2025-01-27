package io.writeopia

import io.writeopia.api.OllamaApi

class OllamaRepository(private val ollamaApi: OllamaApi) {

    suspend fun generateReply(model: String, prompt: String): String {
        return ollamaApi.generateReply(model, prompt).response
    }
}
