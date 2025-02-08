package io.writeopia

import io.writeopia.api.OllamaApi
import io.writeopia.common.utils.ResultData
import io.writeopia.persistence.OllamaDao
import io.writeopia.requests.ModelsResponse
import io.writeopia.responses.DownloadModelResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class OllamaRepository(
    private val ollamaApi: OllamaApi,
    private val ollamaDao: OllamaDao?
) {

    suspend fun generateReply(model: String, prompt: String, url: String): String {
        return ollamaApi.generateReply(model, prompt, url).response
    }

    fun streamReply(model: String, prompt: String, url: String): Flow<ResultData<String>> =
        ollamaApi.streamReply(model, prompt, url)

    fun getModels(url: String): Flow<ResultData<ModelsResponse>> = ollamaApi.getModels(url)

    suspend fun saveOllamaUrl(id: String, url: String) {
        ollamaDao?.updateConfiguration(id) {
            this.copy(url = url)
        }

        refreshConfiguration(id)
    }

    suspend fun saveOllamaSelectedModel(id: String, model: String) {
        ollamaDao?.updateConfiguration(id) {
            this.copy(selectedModel = model)
        }

        refreshConfiguration(id)
    }

    fun listenForConfiguration(id: String) =
        ollamaDao?.listenForConfiguration(id) ?: MutableStateFlow(null)

    suspend fun refreshConfiguration(id: String) {
        ollamaDao?.refreshStateOfId(id)
    }

    suspend fun getConfiguredOllamaUrl(id: String = "disconnected_user"): String? =
        ollamaDao?.getConfiguration()?.url

    suspend fun deleteModel(model: String, url: String): ResultData<Boolean> =
        ollamaApi.removeModel(model, url)

    fun downloadModel(model: String, url: String): Flow<ResultData<DownloadModelResponse>> =
        ollamaApi.downloadModel(model, url)
}
