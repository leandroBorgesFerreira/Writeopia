package br.com.leandroferreira.storyteller.viewmodel.move

import br.com.leandroferreira.storyteller.normalization.addinbetween.AddInBetween
import br.com.leandroferreira.storyteller.utils.ListStoryData
import org.junit.Assert.*
import org.junit.Test

class SpaceMoveHandlerTest {

    @Test
    fun `it should be possible to switch two images position`() {
        // Images are in odd numbers
        val input = AddInBetween.spaces().insert(ListStoryData.imagesInLine())
        val firstImage = input[1]

        val result = SpaceMoveHandler().handleMove(input, firstImage.id, 4)

        assertEquals(result.last().id, firstImage.id)
    }
}
