package io.writeopia.sdk.persistence.parse

import io.writeopia.sdk.models.id.GenerateId
import io.writeopia.sdk.persistence.utils.imageGroup
import kotlin.test.Test
import kotlin.test.assertEquals

class StoryUnitParseKtTest {

    @Test
    fun `parsing a group of image`() {
        val id = GenerateId.generate()
        val entity = imageGroup().toEntity(id)

        assertEquals("group_image", entity.first().type)

        entity.forEachIndexed { i, entityUnit ->
            assertEquals( id, entityUnit.documentId, "step $i should have a document id")
        }

        val parentId = entity.first().id

        entity.drop(1).forEachIndexed { i, entityUnit ->
            assertEquals(parentId, entityUnit.parentId, "step ${i + 1} should have a parent id")
        }
    }
}

