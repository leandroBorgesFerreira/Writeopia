package io.writeopia.di

import io.writeopia.OllamaRepository
import io.writeopia.api.OllamaApi
import io.writeopia.persistence.OllamaDao
import io.writeopia.persistence.OllamaSqlDao
import io.writeopia.sql.WriteopiaDb

class OllamaInjection(
    private val appConnectionInjection: AppConnectionInjection,
    private val writeopiaDb: WriteopiaDb? = null,
) {

    private fun provideOllamaDao(): OllamaDao = OllamaSqlDao(writeopiaDb?.ollamaEntityQueries)

    private fun provideApi() = OllamaApi(
        client = appConnectionInjection.provideHttpClient(),
        json = appConnectionInjection.provideJson()
    )

    fun provideRepository(ollamaApi: OllamaApi = provideApi()) =
        OllamaRepository(ollamaApi, provideOllamaDao())
}
