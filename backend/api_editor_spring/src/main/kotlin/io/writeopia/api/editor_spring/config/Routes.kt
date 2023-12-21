package io.writeopia.api.editor_spring.config

import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.web.reactive.function.server.coRouter

fun appRouter() = coRouter {
    accept(MediaType.APPLICATION_JSON).nest {
        "/api/writeopia".nest {
            GET("/hi/{name}") { request ->
                ServerResponse.ok().bodyValueAndAwait("Hey!")
            }
        }
    }
}

object Hi {
    fun hi(name: String) = "Hi $name!"
}