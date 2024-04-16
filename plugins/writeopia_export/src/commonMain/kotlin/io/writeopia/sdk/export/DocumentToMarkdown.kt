package io.writeopia.sdk.export

import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.sdk.models.story.StoryTypes
import io.writeopia.sdk.models.story.Tags

/**
 * This class parses a document as a Map<Int, [StoryStep]> to a String following the Markdown
 * syntax.
 */
object DocumentToMarkdown {

    // In the future it may be necessary to add a parse to an OutputStream

    fun parse(
        document: Map<Int, StoryStep>,
        parseStep: (StoryStep) -> Pair<ContentAdd, String?> = ::parseStep,
        prettyPrint: Boolean = false
    ): String =
        document.map { (_, storyStep) -> parseStep(storyStep) }
            .filter { (_, contextText) -> contextText != null }
            .let { contentList ->
                buildString {
                    contentList.forEach { (contentAdd, contextText) ->
                        if (
                            prettyPrint &&
                            (contentAdd == ContentAdd.EMPTY_LINE_BEFORE ||
                                    contentAdd == ContentAdd.EMPTY_LINE_BEFORE_AND_AFTER)
                        ) {
                            appendLine()
                        }

                        appendLine(contextText)

                        if (
                            prettyPrint &&
                            (contentAdd == ContentAdd.EMPTY_LINE_AFTER ||
                                    contentAdd == ContentAdd.EMPTY_LINE_BEFORE_AND_AFTER)
                        ) {
                            appendLine()
                        }
                    }
                }
            }

    private fun parseStep(storyStep: StoryStep): Pair<ContentAdd, String?> =
        when (storyStep.type.number) {
            StoryTypes.TITLE.type.number -> ContentAdd.NOTHING to "# ${storyStep.text}"

            StoryTypes.TEXT.type.number -> parseText(storyStep)

            else -> ContentAdd.NOTHING to storyStep.text
        }

    private fun parseText(storyStep: StoryStep): Pair<ContentAdd, String?> =
        when {
            storyStep.tags.contains(Tags.H1.tag) ->
                ContentAdd.EMPTY_LINE_BEFORE_AND_AFTER to "# ${storyStep.text}"

            storyStep.tags.contains(Tags.H2.tag) ->
                ContentAdd.EMPTY_LINE_BEFORE_AND_AFTER to "## ${storyStep.text}"

            storyStep.tags.contains(Tags.H3.tag) ->
                ContentAdd.EMPTY_LINE_BEFORE_AND_AFTER to "### ${storyStep.text}"

            storyStep.tags.contains(Tags.H4.tag) ->
                ContentAdd.EMPTY_LINE_BEFORE_AND_AFTER to "#### ${storyStep.text}"

            else -> ContentAdd.NOTHING to storyStep.text
        }
}

enum class ContentAdd {
    NOTHING, EMPTY_LINE_BEFORE, EMPTY_LINE_AFTER, EMPTY_LINE_BEFORE_AND_AFTER
}
