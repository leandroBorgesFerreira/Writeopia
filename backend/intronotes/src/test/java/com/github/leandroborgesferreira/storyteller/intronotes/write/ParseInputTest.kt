package com.github.leandroborgesferreira.storyteller.intronotes.write

import com.github.leandroborgesferreira.storyteller.intronotes.input.ParseInput
import com.github.leandroborgesferreira.storyteller.intronotes.utils.Samples
import org.junit.Assert.*
import org.junit.Test

class ParseInputTest {

    @Test
    fun `parse should happen correctly`() {
        val input = Samples.sampleEntity()
        val documentEntity = ParseInput.parse(input)

        assertEquals("ccc56899-d125-488d-9273-7fe674995034", documentEntity.id)
        assertEquals(5, documentEntity.content!!.size)
    }
}