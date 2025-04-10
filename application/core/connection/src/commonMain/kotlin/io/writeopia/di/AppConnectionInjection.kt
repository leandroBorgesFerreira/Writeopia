package io.writeopia.di

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule

class AppConnectionInjection private constructor(
    private val json: Json = Json {
        serializersModule = SerializersModule {
            ignoreUnknownKeys = true
        }
    },
    private val apiLogger: Logger = Logger.DEFAULT
) {
    fun provideJson() = json

    fun provideHttpClient(): HttpClient = ApiInjectorDefaults.httpClient(json, apiLogger)

    companion object {
        private var instance: AppConnectionInjection? = null

        fun singleton(): AppConnectionInjection = instance ?: AppConnectionInjection().also {
            instance = it
        }
    }
}

object ApiInjectorDefaults {
    fun httpClient(
        json: Json,
        apiLogger: Logger,
    ) = HttpClient {
        install(HttpTimeout) {
            requestTimeoutMillis = 300000
            socketTimeoutMillis = 300000
        }

        install(ContentNegotiation) {
            json(json = json)
        }

        install(Logging) {
            logger = apiLogger
            level = LogLevel.ALL
            sanitizeHeader { header -> header == HttpHeaders.Authorization }
        }
    }
}
