package com.github.leandroborgesferreira.storyteller.utils.extensions

import androidx.compose.runtime.toMutableStateMap
import com.github.leandroborgesferreira.storyteller.models.story.StoryStep
import com.github.leandroborgesferreira.storyteller.model.story.StoryTypes
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Test

class StoryExtensionsKtTest {

    @Test
    fun `it should be able to recognize an empty document`() {
        val storyStepMap = buildList {
            repeat(5) {
                add(it to StoryStep(type = StoryTypes.MESSAGE.type))
            }
        }.toMap()

        assertTrue(storyStepMap.noContent())
    }

    @Test
    fun `it should be able to recognize a not empty document`() {
        val storyStepMap = buildList {
            repeat(5) { index ->
                add(index to StoryStep(
                    type = StoryTypes.MESSAGE.type
                )
                )
            }
        }.toMutableStateMap()

        storyStepMap[5] = StoryStep(
            type = StoryTypes.MESSAGE.type,
            text = "some text"
        )

        assertFalse(storyStepMap.noContent())
    }
}