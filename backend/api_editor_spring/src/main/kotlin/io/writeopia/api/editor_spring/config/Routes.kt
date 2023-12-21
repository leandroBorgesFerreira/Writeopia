package io.writeopia.api.editor_spring.config

import io.writeopia.api.editor_spring.EditorHandler
import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.sdk.models.story.StoryTypes
import io.writeopia.sdk.serialization.data.DocumentApi
import io.writeopia.sdk.serialization.extensions.toApi
import kotlinx.datetime.Clock
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.awaitBody
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.web.reactive.function.server.coRouter

fun appRouter(editorHandler: EditorHandler) = coRouter {
    accept(MediaType.APPLICATION_JSON).nest {
        "/api/writeopia".nest {
            GET("/example") {
                ServerResponse.ok().bodyValueAndAwait(editorHandler.example())
            }

            GET("/document/{id}") { request ->
                editorHandler.getDocument(request.pathVariable("id"))
            }

            POST("/document") { request ->
                editorHandler.saveDocument(request.awaitBody())
            }
        }
    }
}

object Hi {
    fun hi(name: String) = "Hi $name!"
}