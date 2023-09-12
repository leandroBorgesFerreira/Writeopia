package com.storiesteller.sdk.intronotes.write

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent
import com.storiesteller.sdk.intronotes.dynamo.introNotesTable
import com.storiesteller.sdk.intronotes.input.ParseInput
import com.storiesteller.sdk.intronotes.persistence.entity.DocumentEntity
import com.storiesteller.sdk.intronotes.persistence.repository.DynamoIntroNotesRepository
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable

fun writeIntroNotes(
    stringBody: String,
    loggerFn: (String) -> Unit,
    loggerErrorFn: (String) -> Unit,
    notesTable: DynamoDbTable<DocumentEntity> = introNotesTable()
): APIGatewayProxyResponseEvent =
    try {
        loggerFn("Received: $stringBody")
        val documentEntity = ParseInput.parse(stringBody)
        DynamoIntroNotesRepository.saveNote(documentEntity, notesTable)

        APIGatewayProxyResponseEvent()
            .withStatusCode(200)
            .withBody("Success")
    } catch (e: Exception) {
        loggerErrorFn("Failed to save notes. Message: ${e.message}")

        APIGatewayProxyResponseEvent()
            .withStatusCode(500)
            .withBody("An error occurred. Received request: $stringBody. Error: ${e.message}")
    }
