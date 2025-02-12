package io.writeopia.sdk.manager

import io.writeopia.sdk.models.span.Interception
import io.writeopia.sdk.models.span.Span
import io.writeopia.sdk.models.span.SpanInfo
import io.writeopia.sdk.models.story.StoryStep
import kotlin.math.min

object SpansHandler {

    fun toggleSpans(spanSet: Set<SpanInfo>, newSpan: SpanInfo): Set<SpanInfo> {
        return when {
            spanSet.contains(newSpan) -> spanSet - newSpan

            !spanSet.any { it.span == newSpan.span } -> spanSet + newSpan

            else -> {
                val currentSpan = spanSet.first { it.span == newSpan.span }

                val intersection: Interception = currentSpan.intersection(newSpan)

                return when (intersection) {
                    Interception.INSIDE -> {
                        val removed = (spanSet - currentSpan)

                        val splitSpans = setOf(
                            SpanInfo(
                                currentSpan.start,
                                newSpan.start,
                                currentSpan.span
                            ),
                            SpanInfo(
                                newSpan.end,
                                currentSpan.end,
                                currentSpan.span
                            ),
                        ).filter { it.size() > 0 }

                        removed + splitSpans
                    }
                    Interception.INTERSECT -> {
                        val removed = (spanSet - currentSpan)
                        val minStart = min(currentSpan.start, newSpan.start)
                        val maxEnd = min(currentSpan.end, newSpan.end)

                        removed + SpanInfo(minStart, maxEnd, newSpan.span)
                    }

                    Interception.OUTSIDE -> spanSet + newSpan

                    Interception.CONTAINING -> {
                        val removed = (spanSet - currentSpan)
                        removed + newSpan
                    }
                }
            }
        }
    }

    fun toggleSpansForManyStories(
        storySteps: Map<Int, StoryStep>,
        newSpan: Span
    ): Map<Int, StoryStep> =
        if (storySteps.all { (_, story) -> story.spans.any { it.span == newSpan } }) {
            storySteps.mapValues { (_, story) ->
                val removedSpans = story.spans.filterTo(mutableSetOf()) { it.span != newSpan }
                story.copy(spans = removedSpans)
            }
        } else {
            storySteps.mapValues { (_, story) ->
                val text = story.text
                if (text?.isNotEmpty() == true) {
                    val newSpanInfo = SpanInfo(0, text.length, newSpan)
                    story.copy(spans = story.spans + newSpanInfo)
                } else {
                    story
                }
            }
        }
}
