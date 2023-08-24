package com.github.leandroborgesferreira.storyteller.intronotes.unit

import com.github.leandroborgesferreira.storyteller.intronotes.input.ParseInput
import com.github.leandroborgesferreira.storyteller.intronotes.unit.utils.Samples
import org.junit.Assert.*
import org.junit.Test

class ParseInputTest {

    @Test
    fun `parse should happen correctly`() {
        val id = "fakeId"

        val input = Samples.sampleEntity(id, "title")
        val documentEntity = ParseInput.parse(input)

        assertEquals(id, documentEntity.id)
        assertEquals(5, documentEntity.content!!.size)
    }
}