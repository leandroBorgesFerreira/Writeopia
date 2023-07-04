package com.github.leandroborgesferreira.storyteller.utils.extensions

import androidx.compose.runtime.toMutableStateMap
import com.github.leandroborgesferreira.storyteller.model.story.StoryStep
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Test

class StoryExtensionsKtTest {

    @Test
    fun `it should be able to recognize an empty document`() {
        val storyStepMap = buildList {
            repeat(5) {
                add(it to StoryStep(type = "message"))
            }
        }.toMap()

        assertTrue(storyStepMap.noContent())
    }

    @Test
    fun `it should be able to recognize a not empty document`() {
        val storyStepMap = buildList {
            repeat(5) { index ->
                add(index to StoryStep(type = "message"))
            }
        }.toMutableStateMap()

        storyStepMap[5] = StoryStep(type = "message", text = "some text")

        assertFalse(storyStepMap.noContent())
    }
}