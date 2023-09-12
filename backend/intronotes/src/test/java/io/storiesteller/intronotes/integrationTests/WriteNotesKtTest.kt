package io.storiesteller.intronotes.integrationTests

import io.storiesteller.intronotes.dynamo.introNotesTable
import io.storiesteller.intronotes.persistence.repository.DynamoIntroNotesRepository
import io.storiesteller.intronotes.persistence.repository.INTRO_NOTES_TABLE
import io.storiesteller.intronotes.unit.utils.Samples
import io.storiesteller.intronotes.write.writeIntroNotes
import org.junit.Assert.*
import org.junit.Test
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.AttributeDefinition
import software.amazon.awssdk.services.dynamodb.model.CreateTableRequest
import software.amazon.awssdk.services.dynamodb.model.KeySchemaElement
import software.amazon.awssdk.services.dynamodb.model.KeyType
import software.amazon.awssdk.services.dynamodb.model.ProvisionedThroughput
import software.amazon.awssdk.services.dynamodb.model.ScalarAttributeType
import java.net.URI

class WriteNotesKtTest {

    @Test
    fun `it should be possible to save note correctly`() {
        val dynamoDbClient = DynamoDbClient.builder()
            .region(Region.US_EAST_1)
            .endpointOverride(URI.create("http://localhost:8000"))
            .credentialsProvider(
                StaticCredentialsProvider.create(
                    AwsBasicCredentials.create("FAKE", "FAKE")
                )
            )
            .build()

        val id = "fakeId"
        val title = "fakeTitle"

        val input = Samples.sampleEntity(id, title)
        val table = introNotesTable(
            tableName = INTRO_NOTES_TABLE,
            dynamoClient = dynamoDbClient
        )

        val result = writeIntroNotes(
            input,
            loggerFn = {},
            loggerErrorFn = {},
            notesTable = table
        )

        assertEquals(200, result.statusCode)
        val note = DynamoIntroNotesRepository.readNote(id, table)

        assertEquals(id, note.id)
    }
}
