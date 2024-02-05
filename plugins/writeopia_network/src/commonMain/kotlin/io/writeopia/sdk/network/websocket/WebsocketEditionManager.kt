package io.writeopia.sdk.network.websocket

import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.websocket.*
import io.writeopia.sdk.model.document.DocumentInfo
import io.writeopia.sdk.model.story.StoryState
import io.writeopia.sdk.shared_edition.SharedEditionManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class WebsocketEditionManager(
    private val host: String = "127.0.0.1",
    private val client: HttpClient,
    private val json: Json
) : SharedEditionManager {

    private var websocketSection: DefaultClientWebSocketSession? = null

    override suspend fun startLiveEdition(
        inFlow: Flow<Pair<StoryState, DocumentInfo>>,
        outFlow: MutableStateFlow<StoryState>
    ) {
        client.webSocket(method = HttpMethod.Get, host = host, port = 8080, path = "/chat") {
            outputMessages(outFlow)
            inputMessages(inFlow)

            websocketSection = this
        }
    }

    override suspend fun stopLiveEdition() {
        websocketSection?.close()
    }

    private suspend fun DefaultClientWebSocketSession.outputMessages(outFlow: MutableStateFlow<StoryState>) {
        try {
            for (message in incoming) {
                message as? Frame.Text ?: continue
                val storyState: StoryState = json.decodeFromString(message.readText())
                outFlow.emit(storyState)
            }
        } catch (e: Exception) {
            println("Error while receiving: " + e.message)
        }
    }

    private suspend fun DefaultClientWebSocketSession.inputMessages(storyStepFlow: Flow<Pair<StoryState, DocumentInfo>>) {
        storyStepFlow.collect { text ->
            try {
                send(json.encodeToString(text))
            } catch (e: Exception) {
                println("Error while sending: " + e.message)
            }
        }
    }
}
