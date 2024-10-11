package io.writeopia.plugins

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.routing.routing
import io.ktor.server.websocket.WebSockets
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import io.ktor.websocket.send
import io.writeopia.socket.Connection
import java.util.Collections

fun Application.configureSockets() {
    install(WebSockets) {
//        pingPeriod = Duration.ofSeconds(15)
//        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }

    routing {
        val connections = Collections.synchronizedSet<Connection?>(LinkedHashSet())

        webSocket("/editor") {
            val thisConnection = Connection(this)
            connections += thisConnection

            try {
                send("You are connected! There are ${connections.count()} users here.")

                for (frame in incoming) {
                    frame as? Frame.Text ?: continue
                    val receivedText = frame.readText()
                    val messageToSend = "[${thisConnection.name}]: $receivedText"
                    connections.forEach { connection ->
                        connection.session.send(messageToSend)
                    }
                }
            } catch (e: Exception) {
                println(e.localizedMessage)
            } finally {
                println("Removing $thisConnection!")
                connections -= thisConnection
            }
        }
    }
}
