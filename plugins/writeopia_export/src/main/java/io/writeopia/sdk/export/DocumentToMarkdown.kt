package io.writeopia.sdk.export

import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.sdk.models.story.StoryTypes

object DocumentToMarkdown {

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
            StoryTypes.H1.type.number ->
                ContentAdd.EMPTY_LINE_BEFORE_AND_AFTER to "# ${storyStep.text}"

            StoryTypes.H2.type.number ->
                ContentAdd.EMPTY_LINE_BEFORE_AND_AFTER to "## ${storyStep.text}"

            StoryTypes.H3.type.number ->
                ContentAdd.EMPTY_LINE_BEFORE_AND_AFTER to "### ${storyStep.text}"

            StoryTypes.H4.type.number ->
                ContentAdd.EMPTY_LINE_BEFORE_AND_AFTER to "#### ${storyStep.text}"

            else -> ContentAdd.NOTHING to storyStep.text
        }
}

enum class ContentAdd {
    NOTHING, EMPTY_LINE_BEFORE, EMPTY_LINE_AFTER, EMPTY_LINE_BEFORE_AND_AFTER
}
