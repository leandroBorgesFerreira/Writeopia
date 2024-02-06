package io.writeopia

import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import io.writeopia.sdk.serialization.data.DocumentApi
import io.writeopia.sdk.serialization.json.writeopiaJson
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {
    @Test
    fun itShouldBePossibleToGetAnExampleDocument() = testApplication {
        application {
            module()
        }

        val client = clientClient()

        client.get("/api/document/example").run {
            assertEquals(HttpStatusCode.OK, status)
            body<DocumentApi>()
        }
    }

    private fun ApplicationTestBuilder.clientClient() = createClient {
        install(ContentNegotiation) {
            json(json = writeopiaJson)
        }
    }
}
