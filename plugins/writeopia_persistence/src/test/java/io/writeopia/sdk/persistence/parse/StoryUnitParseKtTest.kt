package io.writeopia.sdk.persistence.parse

import io.writeopia.sdk.models.id.GenerateId
import io.writeopia.sdk.persistence.utils.imageGroup
import org.junit.Assert.*
import org.junit.Test

class StoryUnitParseKtTest {

    @Test
    fun `parsing a group of image`() {
        val id = GenerateId.generate()
        val entity = imageGroup().toEntity(id)

        assertEquals("group_image", entity.first().type)

        entity.forEachIndexed { i, entityUnit ->
            assertEquals("step $i should have a document id", id, entityUnit.documentId)
        }

        val parentId = entity.first().id

        entity.drop(1).forEachIndexed { i, entityUnit ->
            assertEquals(
                "step ${i + 1} should have a parent id",
                parentId, entityUnit.parentId
            )
        }
    }
}

