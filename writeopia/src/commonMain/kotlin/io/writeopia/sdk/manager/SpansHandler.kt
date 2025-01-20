package io.writeopia.sdk.manager

import io.writeopia.sdk.models.span.Interception
import io.writeopia.sdk.models.span.SpanInfo
import kotlin.math.min

object SpansHandler {

    fun addSpan(spanSet: Set<SpanInfo>, newSpan: SpanInfo): Set<SpanInfo> {
        return when {
            spanSet.contains(newSpan) -> spanSet - newSpan

            !spanSet.any { it.span == newSpan.span } -> spanSet + newSpan

            else -> {
                val currentSpan = spanSet.first { it.span == newSpan.span }

                val intersection: Interception = currentSpan.intersection(newSpan)

                return when (intersection) {
                    Interception.INSIDE -> spanSet
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
}
