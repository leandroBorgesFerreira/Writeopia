package br.com.leandroferreira.storyteller.normalization.merge.steps

import br.com.leandroferreira.storyteller.model.GroupStep
import br.com.leandroferreira.storyteller.model.StoryStep
import org.junit.Assert.assertEquals
import org.junit.Test

class StepToGroupMergerTest {

    @Test
    fun `a simple merge should be possible`() {
        val merger = StepToGroupMerger()

        val image1 = StoryStep(
            id = "1",
            type = "image",
            localPosition = 0
        )

        val image2 = StoryStep(
            id = "2",
            type = "image",
            localPosition = 0
        )

        val result = merger.merge(image1, image2, "group_image")

        assertEquals("group_image", result.type)
        assertEquals(2, (result as GroupStep).steps.size)
        assertEquals(result.id, result.steps[0].parentId)
        assertEquals(result.id, result.steps[1].parentId)
    }
}
