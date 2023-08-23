package com.github.leandroborgesferreira.storyteller.intronotes.write

import com.github.leandroborgesferreira.storyteller.intronotes.dynamo.introNotesTable
import com.github.leandroborgesferreira.storyteller.intronotes.persistence.repository.IntroNotesRepository
import com.github.leandroborgesferreira.storyteller.intronotes.utils.Samples
import org.junit.Assert.*
import org.junit.Test
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.ContainerCredentialsProvider
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import java.net.URI

class WriteNotesKtTest {

    @Test
    fun `it should be possible to save not correctly`() {

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
            tableName = "IntroNotesLocal",
            dynamoClient = dynamoDbClient
        )

        val result = writeIntroNotes(
            input,
            loggerFn = {},
            loggerErrorFn = {},
            notesTable = table
        )

        assertEquals(200, result.statusCode)

        val note = IntroNotesRepository.readNote(table, id, title)

        assertEquals(id, note.id)
    }
}