package io.writeopia

import io.writeopia.api.OllamaApi
import io.writeopia.common.utils.ResultData
import io.writeopia.requests.ModelsResponse
import kotlinx.coroutines.flow.Flow

class OllamaRepository(private val ollamaApi: OllamaApi) {

    suspend fun generateReply(model: String, prompt: String): String {
        return ollamaApi.generateReply(model, prompt).response
    }

    fun streamReply(model: String, prompt: String): Flow<ResultData<String>> =
        ollamaApi.streamReply(model, prompt)

    fun getModels(): Flow<ModelsResponse> = ollamaApi.getModels()
}
