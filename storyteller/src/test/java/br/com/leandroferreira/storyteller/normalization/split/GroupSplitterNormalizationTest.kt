package br.com.leandroferreira.storyteller.normalization.split

import br.com.leandroferreira.storyteller.utils.ListStoryData
import org.junit.Assert.*
import org.junit.Test

class GroupSplitterNormalizationTest {

    @Test
    fun `it should be possible to detach an image from a GroupImage`() {
        val input = ListStoryData.imageGroupToDetach()
        val initialSize = input.size
        val result = GroupSplitterNormalization.split(input)

        assertEquals(initialSize + 1, result.size)

        result.forEachIndexed { i, unit ->
            assertEquals(i, unit.localPosition)
        }
    }
}
