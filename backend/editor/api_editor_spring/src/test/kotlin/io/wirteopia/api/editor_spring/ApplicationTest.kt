package io.wirteopia.api.editor_spring

import io.writeopia.api.editor.WriteopiaEditorApi
import io.writeopia.api.editor_spring.EditorHandler
import io.writeopia.api.editor_spring.config.appRouter
import io.writeopia.sdk.serialization.data.DocumentApi
import org.junit.Test
import org.springframework.test.web.reactive.server.WebTestClient


class ApplicationTest {

    private val webClient = WebTestClient
        .bindToRouterFunction(appRouter(EditorHandler(WriteopiaEditorApi.create())))
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
    

}