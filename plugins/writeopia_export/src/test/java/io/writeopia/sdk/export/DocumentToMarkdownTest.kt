package io.writeopia.sdk.export

import io.writeopia.sdk.model.story.StoryTypes
import io.writeopia.sdk.models.story.StoryStep
import org.junit.Assert.*
import org.junit.Test

class DocumentToMarkdownTest {

    @Test
    fun `test for markdown parse for one liner`() {
        val content = mapOf(
            0 to StoryStep(type = StoryTypes.H1.type, text = "Title!!"),
            1 to StoryStep(type = StoryTypes.MESSAGE.type, text = "some text"),
            2 to StoryStep(type = StoryTypes.H2.type, text = "Subtitle!!")
        )

        val parsedContent = DocumentToMarkdown.parse(content)
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
}
