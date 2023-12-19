package io.writeopia.extensions

import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.sdk.models.story.StoryTypes
import io.writeopia.sdk.utils.extensions.noContent
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class StoryExtensionsKtTest {

    @Test
    fun `it should be able to recognize an empty document`() {
        val storyStepMap = buildList {
            repeat(5) {
                add(it to StoryStep(type = StoryTypes.TEXT.type))
            }
        }.toMap()

        assertTrue(storyStepMap.noContent())
    }

    @Test
    fun `it should be able to recognize a not empty document`() {
        val storyStepMap = buildList {
            repeat(5) { index ->
                add(index to StoryStep(
                    type = StoryTypes.TEXT.type
                )
                )
            }
        }.toMap().toMutableMap()

        storyStepMap[5] = StoryStep(
            type = StoryTypes.TEXT.type,
            text = "some text"
        )

        assertFalse(storyStepMap.noContent())
    }
}