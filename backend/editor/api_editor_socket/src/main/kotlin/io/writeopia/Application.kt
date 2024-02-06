package io.writeopia

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.writeopia.plugins.configureRouting
import io.writeopia.plugins.configureSerialization
import io.writeopia.plugins.configureSockets

fun main() {
    embeddedServer(
        Netty,
        port = 8080,
        host = "127.0.0.1",
        module = Application::module
    ).start(wait = true)
}

fun Application.module() {
    configureSockets()
    configureRouting()
    configureSerialization()
}
