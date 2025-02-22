package io.writeopia.controller

import io.writeopia.common.utils.ResultData
import io.writeopia.common.utils.download.DownloadState
import kotlinx.coroutines.flow.StateFlow

interface OllamaConfigController {

    val ollamaSelectedModelState: StateFlow<String>

    val ollamaUrl: StateFlow<String>

    val modelsForUrl: StateFlow<ResultData<List<String>>>

    val downloadModelState: StateFlow<ResultData<DownloadState>>

    fun changeOllamaUrl(url: String)

    fun selectOllamaModel(model: String)

    fun retryModels()

    fun modelToDownload(model: String)

    fun deleteModel(model: String)
}
