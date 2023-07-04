package com.github.leandroborgesferreira.storyteller.utils.extensions

import com.github.leandroborgesferreira.storyteller.model.story.StoryStep
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
}