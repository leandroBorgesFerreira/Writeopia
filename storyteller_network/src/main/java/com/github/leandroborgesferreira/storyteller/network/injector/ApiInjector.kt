package com.github.leandroborgesferreira.storyteller.network.injector

import com.github.leandroborgesferreira.storyteller.network.notes.NotesApi
import com.github.leandroborgesferreira.storyteller.serialization.json.storyTellerJson
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class ApiInjector(
    private val client: HttpClient = ApiInjectorDefaults.httpClientJson(),
    private val baseUrl: String = ApiInjectorDefaults.baseUrl(),
) {

    fun notesApi(): NotesApi = NotesApi(client, baseUrl)
}

internal object ApiInjectorDefaults {
    fun httpClientJson(json: Json = storyTellerJson) = HttpClient {
        install(ContentNegotiation) {
            json(json = json)
            Auth {
                bearer {
                    loadTokens {
                        // Load tokens from a local storage and return them as the 'BearerTokens' instance
                        BearerTokens("abc123", "xyz111")
                    }
                }
            }
        }
    }

    fun baseUrl(): String = "https://7787c3dvlj.execute-api.us-east-1.amazonaws.com/prod/"
}

