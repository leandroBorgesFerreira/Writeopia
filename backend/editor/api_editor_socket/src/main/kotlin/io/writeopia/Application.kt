package io.writeopia

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.writeopia.api.editor.WriteopiaEditorApi
import io.writeopia.plugins.configureFirebase
import io.writeopia.plugins.configureRouting
import io.writeopia.plugins.configureSerialization
import io.writeopia.plugins.configureSockets

fun main() {
    embeddedServer(
        Netty,
        port = 8080,
        host = "0.0.0.0",
        module = Application::module
    ).start(wait = true)
}

fun Application.module(byPassAuth: Boolean = false, forceDbInMemory: Boolean? = null) {
    val dbInMemory =
        forceDbInMemory ?: System.getenv("IN_MEMORY_DATABASE")?.let { it == "true" } ?: false

    if (!byPassAuth) {
        configureFirebase()
    }
    configureSockets()
    configureRouting(
        writeopiaEditorApi = WriteopiaEditorApi.create(log, dbInMemory),
        byPassAuth = byPassAuth
    )
    configureSerialization()
}
