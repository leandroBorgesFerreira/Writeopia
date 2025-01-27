package io.writeopia.di

import io.writeopia.OllamaRepository
import io.writeopia.api.OllamaApi
import kotlinx.serialization.json.Json

class OllamaInjection(
    private val appConnectionInjection: AppConnectionInjection,
    private val baseUrl: String = "http://localhost:11434/api",
) {

    private fun provideApi() = OllamaApi(
        client = appConnectionInjection.provideHttpClient(),
        baseUrl = baseUrl,
        json = appConnectionInjection.provideJson()
    )

    fun provideRepository(ollamaApi: OllamaApi = provideApi()) = OllamaRepository(ollamaApi)
}
