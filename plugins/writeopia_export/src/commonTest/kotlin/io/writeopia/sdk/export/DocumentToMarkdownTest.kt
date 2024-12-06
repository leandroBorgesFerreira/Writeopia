package io.writeopia.sdk.export

import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.sdk.models.story.StoryTypes
import io.writeopia.sdk.models.story.Tag
import kotlin.test.Test
import kotlin.test.assertEquals

class DocumentToMarkdownTest {

    @Test
    fun testMarkDown() {
        val content = mapOf(
            0 to StoryStep(
                type = StoryTypes.TEXT.type,
                text = "Title!!",
                tags = setOf(Tag.H1.label)
            ),
            1 to StoryStep(type = StoryTypes.TEXT.type, text = "some text"),
            2 to StoryStep(
                type = StoryTypes.TEXT.type,
                text = "Subtitle!!",
                tags = setOf(Tag.H2.label)
            )
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
    fun testMarkdownPrettyPrintFalse() {
        val content = mapOf(
            0 to StoryStep(
                type = StoryTypes.TEXT.type,
                text = "Title!!",
                tags = setOf(Tag.H1.label)
            ),
            1 to StoryStep(type = StoryTypes.TEXT.type, text = "some text"),
            2 to StoryStep(
                type = StoryTypes.TEXT.type,
                text = "Subtitle!!",
                tags = setOf(Tag.H2.label)
            )
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
