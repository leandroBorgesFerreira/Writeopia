package br.com.leandroferreira.storyteller.viewmodel

import br.com.leandroferreira.storyteller.utils.ListStoryData
import org.junit.Assert.*
import org.junit.Test

class FindStoryTest {

    @Test
    fun `it should be possible to find a storyUnit inside a GroupStep`() {
        val idToSearch = "1"

        val input = ListStoryData.imageGroup()
        val result = FindStory.findById(input, idToSearch)

        assertEquals(idToSearch, result?.first?.id)
    }
}
