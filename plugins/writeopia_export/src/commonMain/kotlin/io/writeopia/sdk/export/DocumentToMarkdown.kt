package io.writeopia.sdk.export

import io.writeopia.sdk.export.files.KmpFileWriter
import io.writeopia.sdk.export.files.name
import io.writeopia.sdk.models.document.Document
import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.sdk.models.story.StoryTypes
import io.writeopia.sdk.models.story.Tags
import io.writeopia.sdk.utils.files.useKmp

/**
 * This class parses a document as a Map<Int, [StoryStep]> to a String following the Markdown
 * syntax.
 */
object DocumentToMarkdown: DocumentWriter {

    // In the future it may be necessary to add a parse to an OutputStream

    override fun writeDocuments(documents: List<Document>, path: String, addHashTable: Boolean) {
        documents.forEach { document ->
            KmpFileWriter(name(document, path, ".md")).useKmp { writer ->
                writeToWriter(
                    content = document.content,
                    kmpFileWriter = writer
                )
            }
        }
    }

    fun parse(
        document: Map<Int, StoryStep>,
        parseStep: (StoryStep) -> Pair<ContentAdd, String?> = ::parseStep,
        prettyPrint: Boolean = false,
    ): String =
        buildString {
            writeTo(document, parseStep, prettyPrint) { line ->
                if (line != null) {
                    appendLine(line)
                } else {
                    appendLine()
                }
            }
        }

    private fun writeToWriter(
        content: Map<Int, StoryStep>,
        kmpFileWriter: KmpFileWriter,
        parseStep: (StoryStep) -> Pair<ContentAdd, String?> = ::parseStep,
        prettyPrint: Boolean = false
    ) {
        writeTo(content, parseStep, prettyPrint, kmpFileWriter::writeLine)
    }

    private fun writeTo(
        content: Map<Int, StoryStep>,
        parseStep: (StoryStep) -> Pair<ContentAdd, String?> = ::parseStep,
        prettyPrint: Boolean = false,
        writeFn: (String?) -> Unit
    ) {
        content
            .asSequence()
            .map { (_, storyStep) -> parseStep(storyStep) }
            .filter { (_, contextText) -> contextText != null }
            .let { contentList ->
                contentList.forEach { (contentAdd, contextText) ->
                    if (
                        prettyPrint &&
                        (contentAdd == ContentAdd.EMPTY_LINE_BEFORE ||
                                contentAdd == ContentAdd.EMPTY_LINE_BEFORE_AND_AFTER)
                    ) {
                        writeFn(null)
                    }

                    writeFn(contextText)

                    if (
                        prettyPrint &&
                        (contentAdd == ContentAdd.EMPTY_LINE_AFTER ||
                                contentAdd == ContentAdd.EMPTY_LINE_BEFORE_AND_AFTER)
                    ) {
                        writeFn(null)
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
