package com.github.leandroborgesferreira.storyteller.network.injector

import com.github.leandroborgesferreira.storyteller.network.notes.NotesApi
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json

class ApiInjector(
    private val client: HttpClient = ApiInjectorDefaults.httpClientJson(),
    private val baseUrl: String = ApiInjectorDefaults.baseUrl()
) {

    fun notesApi(): NotesApi = NotesApi(client, baseUrl)
}

internal object ApiInjectorDefaults {
    fun httpClientJson() = HttpClient {
        install(ContentNegotiation) {
            json()
        }
    }

    fun baseUrl(): String = "https://7787c3dvlj.execute-api.us-east-1.amazonaws.com/prod/"
}

