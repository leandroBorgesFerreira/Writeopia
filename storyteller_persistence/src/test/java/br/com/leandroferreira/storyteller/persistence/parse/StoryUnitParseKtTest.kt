package br.com.leandroferreira.storyteller.persistence.parse

import br.com.leandroferreira.storyteller.model.story.GroupStep
import br.com.leandroferreira.storyteller.model.story.StoryStep
import br.com.leandroferreira.storyteller.model.story.StoryUnit
import br.com.leandroferreira.storyteller.persistence.utils.imageGroup
import org.junit.Assert.*
import org.junit.Test
import java.util.UUID

class StoryUnitParseKtTest {

    @Test
    fun `parsing a group of image`() {
        val id = UUID.randomUUID().toString()
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

