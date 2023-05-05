package br.com.leandroferreira.storyteller.viewmodel.move

import br.com.leandroferreira.storyteller.model.change.MoveInfo
import br.com.leandroferreira.storyteller.normalization.addinbetween.AddInBetween
import br.com.leandroferreira.storyteller.utils.ListStoryData
import br.com.leandroferreira.storyteller.utils.MapStoryData
import org.junit.Assert.*
import org.junit.Test

class SpaceMoveHandlerTest {

    @Test
    fun `it should be possible to switch two images position`() {
        // Images are in odd numbers
        val input = AddInBetween.spaces().insert(MapStoryData.imagesInLine())
        val firstImage = input[1]!!

        val result = SpaceMoveHandler().handleMove(input, MoveInfo(firstImage, 1, 2))

        assertEquals(result[2], firstImage)
    }
}
