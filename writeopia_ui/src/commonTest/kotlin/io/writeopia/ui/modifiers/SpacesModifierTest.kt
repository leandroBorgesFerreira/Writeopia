package io.writeopia.ui.modifiers

import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.sdk.models.story.StoryTypes
import io.writeopia.sdk.models.story.Tag
import io.writeopia.ui.model.DrawStory
import kotlin.test.Test
import kotlin.test.assertEquals

class SpacesModifierTest {

    @Test
    fun `tags should be merged correctly`() {
        val input = listOf(
            StoryStep(type = StoryTypes.TEXT.type, tags = setOf(Tag.HIGH_LIGHT_BLOCK)),
            StoryStep(type = StoryTypes.TEXT.type, tags = setOf(Tag.HIGH_LIGHT_BLOCK))
        ).map {
            DrawStory(it, 0)
        }

        val result = SpacesModifier.modify(input, 0).map { it.storyStep.tags }.flatten()
        val expected = listOf(
            Tag.HIGH_LIGHT_BLOCK.apply { this.position = -1 },
            Tag.HIGH_LIGHT_BLOCK.apply { this.position = 0 },
            Tag.HIGH_LIGHT_BLOCK.apply { this.position = 1 },
        )

        result.zip(expected) { resultItem, expectItem ->
            assertEquals(resultItem.tag, expectItem.tag)
            assertEquals(resultItem.position, expectItem.position)
        }

        assertEquals(expected, result)
    }
}
