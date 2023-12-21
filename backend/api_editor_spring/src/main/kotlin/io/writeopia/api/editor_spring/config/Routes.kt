package io.writeopia.api.editor_spring.config

import io.writeopia.api.editor_spring.EditorHandler
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.awaitBody
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.web.reactive.function.server.coRouter

fun appRouter(editorHandler: EditorHandler) = coRouter {
    accept(MediaType.APPLICATION_JSON).nest {
        "/api/writeopia".nest {
            GET("/hi/{name}") { request ->
                ServerResponse.ok().bodyValueAndAwait("Hey!")
            }

            GET("/document/{id}") { request ->
                editorHandler.getDocument(request.path())
            }

            POST("/document/{id}") { request ->
                editorHandler.saveDocument(request.awaitBody())
            }
        }
    }
}

object Hi {
    fun hi(name: String) = "Hi $name!"
}