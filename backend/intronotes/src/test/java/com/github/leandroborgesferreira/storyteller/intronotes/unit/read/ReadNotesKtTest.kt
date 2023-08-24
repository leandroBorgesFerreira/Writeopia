package com.github.leandroborgesferreira.storyteller.intronotes.unit.read

import com.github.leandroborgesferreira.storyteller.intronotes.extensions.toAPi
import com.github.leandroborgesferreira.storyteller.intronotes.persistence.entity.DocumentEntity
import com.github.leandroborgesferreira.storyteller.intronotes.read.readNotes
import com.github.leandroborgesferreira.storyteller.serialization.json.storyTellerJson
import com.github.leandroborgesferreira.storyteller.serialization.request.wrapInRequest
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

        val expected = storyTellerJson.encodeToString(
            documentEntity.toAPi()
                .let(::listOf)
                .wrapInRequest()
        )

        assertEquals(expected, result)
    }
}