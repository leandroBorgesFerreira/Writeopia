package io.writeopia.sdk.presentation.parse

import kotlin.test.Test
import kotlin.test.assertEquals

class DocumentParserTest {

    @Test
    fun isShouldBePossibleToParseADocumentCorrectly() {
        val (document, expected) = Fixture.document()
        val presentation = PresentationParser.parse(document)

        assertEquals(presentation.size, 3)
        assertEquals(presentation, expected)
    }
}
