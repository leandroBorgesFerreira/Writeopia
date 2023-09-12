package com.github.leandroborgesferreira.storyteller.intronotes

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent
import com.github.leandroborgesferreira.storyteller.intronotes.persistence.repository.DynamoIntroNotesRepository
import com.github.leandroborgesferreira.storyteller.intronotes.read.readNotes
import com.storiesteller.sdk.serialization.json.storyTellerJson

class ReadIntroNotes : RequestHandler<Unit, APIGatewayProxyResponseEvent> {
    override fun handleRequest(input: Unit, context: Context): APIGatewayProxyResponseEvent {
        val response = readNotes(
            json = storyTellerJson,
            provideNote = {
                DynamoIntroNotesRepository.readNote("87eb231f-c263-4562-9d86-ce980bf954a7")
            }
        )

        return APIGatewayProxyResponseEvent()
            .withStatusCode(200)
            .withIsBase64Encoded(false)
            .withBody(response)
    }
}

