package io.writeopia.plugins

import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.writeopia.sdk.serialization.json.writeopiaJson

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json(json = writeopiaJson)
    }
}