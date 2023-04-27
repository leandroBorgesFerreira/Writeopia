package br.com.leandroferreira.storyteller.viewmodel

import br.com.leandroferreira.storyteller.utils.StoryData
import org.junit.Assert.*
import org.junit.Test

class FindStoryTest {

    @Test
    fun `it should be possible to find a storyUnit inside a GroupStep`() {
        val idToSearch = "1"

        val input = StoryData.imageGroup().associateBy { storyUnit -> storyUnit.localPosition }
        val result = FindStory.findById(input, idToSearch)

        assertEquals(idToSearch, result?.id)
    }
}
