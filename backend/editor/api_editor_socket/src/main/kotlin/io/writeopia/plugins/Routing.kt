package io.writeopia.plugins

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("api/editor/socket/hi") {
            call.respondText("Hello World!")
        }
    }
}
