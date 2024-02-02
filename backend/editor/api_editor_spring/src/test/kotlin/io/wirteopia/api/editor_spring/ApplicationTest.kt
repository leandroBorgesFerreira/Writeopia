package io.wirteopia.api.editor_spring

import io.writeopia.api.editor.WriteopiaEditorApi
import io.writeopia.api.editor.utils.example
import io.writeopia.api.editor_spring.EditorHandler
import io.writeopia.api.editor_spring.config.appRouter
import io.writeopia.app.endpoints.EndPoints
import io.writeopia.sdk.serialization.data.DocumentApi
import org.springframework.core.ParameterizedTypeReference
import org.springframework.test.web.reactive.server.WebTestClient
import kotlin.test.Test


class ApplicationTest {

    private val router = appRouter(EditorHandler(WriteopiaEditorApi.create()), byPassAuth = true)

    private val webClient = WebTestClient
        .bindToRouterFunction(router)
        .build()

    @Test
    fun itShouldBePossibleToGetAnExampleDocument() {
        webClient.get()
            .uri("/api/document/example")
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(DocumentApi::class.java)
    }

    @Test
    fun itShouldBePossibleToGetIntroNotes() {
        webClient.get()
            .uri("/api/${EndPoints.introNotes()}")
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(object : ParameterizedTypeReference<List<DocumentApi>>() {})
    }

    @Test
    fun itShouldBePossibleToSaveAndGetDocuments() {
        val id = "mockId"

        webClient.post()
            .uri("/api/document")
            .bodyValue(DocumentApi.example(id = id))
            .exchange()
            .expectStatus()
            .isAccepted

        webClient.get()
            .uri("/api/document/$id")
            .exchange()
            .expectStatus()
            .isOk
            .expectBody(DocumentApi::class.java)
    }
}
