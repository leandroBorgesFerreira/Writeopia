package io.writeopia.sdk.models.span

data class SpanInfo(val start: Int, val end: Int, val span: Span) {

    fun size() = end - start

    fun intersection(spanInfo: SpanInfo): Interception {
        val (smaller, bigger) = if (this.size() > spanInfo.size()) {
            spanInfo to this
        } else {
            this to spanInfo
        }

        return when {
            isPartiallyInside(smaller, bigger) -> Interception.INTERSECT

            isInside(smaller, bigger) ->
                if (this == bigger) Interception.INSIDE else Interception.CONTAINING

            else -> Interception.OUTSIDE
        }
    }

    private fun isPartiallyInside(smaller: SpanInfo, bigger: SpanInfo): Boolean {
        val insideFromStart = smaller.start < bigger.start &&
            smaller.end > bigger.start &&
            smaller.end < bigger.end

        val insideFromEnd = smaller.start > bigger.start &&
            smaller.start < bigger.end &&
            smaller.end > bigger.end

        return insideFromStart || insideFromEnd
    }

    private fun isInside(smaller: SpanInfo, bigger: SpanInfo): Boolean {
        return smaller.start > bigger.start && smaller.start < bigger.end &&
            smaller.end > bigger.start && smaller.end < bigger.end
    }
}

enum class Interception {
    INSIDE,
    INTERSECT,
    OUTSIDE,
    CONTAINING
}

enum class Span {
    BOLD,
    ITALIC,
    UNDERLINE,
    HIGHLIGHT
}
