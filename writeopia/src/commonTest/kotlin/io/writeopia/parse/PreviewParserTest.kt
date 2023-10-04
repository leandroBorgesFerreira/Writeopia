package io.writeopia.parse

import io.writeopia.sdk.preview.PreviewParser
import io.writeopia.utils.MapStoryData
import kotlin.test.Test
import kotlin.test.assertEquals

class PreviewParserTest {

    @Test
    fun `a simple document should be parsed`() {
        val input = MapStoryData.singleCheckItem().values.toList()
        val result = PreviewParser().preview(input)

        assertEquals(
            input,
            result,
            "The check item provided should be in the preview"
        )
    }

    @Test
    fun `a complex document should be parsed`() {
        val maxSize = 3
        val input = MapStoryData.syncHistory().values.toList()
        val result = PreviewParser().preview(input, maxSize)

        assertEquals(
            maxSize,
            result.size,
            "The check item provided should be in the preview"
        )
    }
}
