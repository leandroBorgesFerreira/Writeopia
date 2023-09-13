package io.writeopia.intronotes.unit.read

import io.writeopia.intronotes.extensions.toAPi
import io.writeopia.intronotes.persistence.entity.DocumentEntity
import io.writeopia.intronotes.read.readNotes
import io.writeopia.sdk.serialization.json.writeopiaJson
import io.writeopia.sdk.serialization.request.wrapInRequest
import kotlinx.serialization.encodeToString
import org.junit.Assert.*
import org.junit.Test

class ReadNotesKtTest {

    /*
     * This test replicated the logic of the readNotes, which is a bit of a 0 == 0 test. But
     * the main goal here is to ensure that no exceptions happen during the serialization,
     * because the serialization can be wrongly configured.
     */
    @Test
    fun `parsing notes should work correctly`() {
        val documentEntity = DocumentEntity(createdAt = 0, lastUpdatedAt = 0)
        val result = readNotes { documentEntity }

        val expected = writeopiaJson.encodeToString(
            documentEntity.toAPi()
                .let(::listOf)
                .wrapInRequest()
        )

        assertEquals(expected, result)
    }
}