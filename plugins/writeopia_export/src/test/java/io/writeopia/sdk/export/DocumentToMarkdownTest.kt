package io.writeopia.sdk.export

import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.sdk.models.story.StoryTypes
import org.junit.Assert.*
import org.junit.Test

class DocumentToMarkdownTest {

    @Test
    fun `test for markdown parse for one liner - pretty print`() {
        val content = mapOf(
            0 to StoryStep(type = StoryTypes.H1.type, text = "Title!!"),
            1 to StoryStep(type = StoryTypes.TEXT.type, text = "some text"),
            2 to StoryStep(type = StoryTypes.H2.type, text = "Subtitle!!")
        )

        val parsedContent = DocumentToMarkdown.parse(content, prettyPrint = true)
        val expected = buildString {
            appendLine()
            appendLine("# Title!!")
            appendLine()
            appendLine("some text")
            appendLine()
            appendLine("## Subtitle!!")
            appendLine()
        }

        assertEquals(expected, parsedContent)
    }

    @Test
    fun `test for markdown parse for one liner - pretty print false`() {
        val content = mapOf(
            0 to StoryStep(type = StoryTypes.H1.type, text = "Title!!"),
            1 to StoryStep(type = StoryTypes.TEXT.type, text = "some text"),
            2 to StoryStep(type = StoryTypes.H2.type, text = "Subtitle!!")
        )

        val parsedContent = DocumentToMarkdown.parse(content, prettyPrint = false)
        val expected = buildString {
            appendLine("# Title!!")
            appendLine("some text")
            appendLine("## Subtitle!!")
        }

        assertEquals(expected, parsedContent)
    }
}
