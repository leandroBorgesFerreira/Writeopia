package com.github.leandroborgesferreira.storyteller.intronotes

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent
import com.github.leandroborgesferreira.storyteller.intronotes.model.StoryStep
import com.github.leandroborgesferreira.storyteller.intronotes.persistence.entity.StoryStepEntity
import com.github.leandroborgesferreira.storyteller.intronotes.persistence.repository.saveNotes
import kotlinx.serialization.json.Json
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class WriteIntroNotes : RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    override fun handleRequest(input: APIGatewayProxyRequestEvent, context: Context): APIGatewayProxyResponseEvent =
        try {
            val logger = context.logger
            val body = input.body

            logger.log("Received: $body")
            val storyStep = Json.decodeFromString<List<StoryStep>>(input.body)

            storyStep.toEntityList().let(::saveNotes)

            APIGatewayProxyResponseEvent()
                .withStatusCode(200)
                .withBody("Success")
        } catch (e: Exception) {
            logger.error("Failed to save notes. Message: ${e.message}")

            APIGatewayProxyResponseEvent()
                .withStatusCode(500)
                .withBody("An error occurred. Received request: $input. Error: ${e.message}")
        }

    private fun List<StoryStep>.toEntityList(): List<StoryStepEntity> =
        this.map { storyStep ->
            StoryStepEntity(
                id = storyStep.id,
                type = storyStep.type.name,
                parentId = storyStep.parentId,
                url = storyStep.url,
                path = storyStep.path,
                text = storyStep.text,
                title = storyStep.title,
                checked = storyStep.checked,
                position = storyStep.position,
            )
        }
}
