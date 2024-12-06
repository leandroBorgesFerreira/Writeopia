package io.writeopia.ui.modifiers

import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.sdk.models.story.StoryTypes
import io.writeopia.sdk.models.story.Tag
import io.writeopia.sdk.models.story.TagInfo
import io.writeopia.ui.model.DrawStory
import kotlin.test.Test
import kotlin.test.assertEquals

class SpacesModifierTest {

    @Test
    fun `tags should be merged correctly`() {
        val input = listOf(
            StoryStep(type = StoryTypes.TEXT.type, tags = setOf(TagInfo(Tag.HIGH_LIGHT_BLOCK))),
            StoryStep(type = StoryTypes.TEXT.type, tags = setOf(TagInfo(Tag.HIGH_LIGHT_BLOCK)))
        ).map {
            DrawStory(it, 0)
        }

        val result = SpacesModifier.modify(input, 0).map { it.storyStep.tags }.flatten()
        val expected = listOf(
            TagInfo(Tag.HIGH_LIGHT_BLOCK, -1),
            TagInfo(Tag.HIGH_LIGHT_BLOCK, 0),
            TagInfo(Tag.HIGH_LIGHT_BLOCK, 1),
        )

        assertEquals(expected, result)
    }
}
