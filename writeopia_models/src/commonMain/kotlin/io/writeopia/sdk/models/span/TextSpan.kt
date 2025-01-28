package io.writeopia.sdk.models.span

data class SpanInfo(val start: Int, val end: Int, val span: Span) {

    fun toText(): String {
        // Serialize the object as a string: "start:end:span"
        return "$start:$end:${span.toText()}"
    }

    fun size() = end - start

    fun intersection(spanInfo: SpanInfo): Interception {
        val (smaller, bigger) = orderSpansBySize(this, spanInfo)

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
        return smaller.start >= bigger.start && smaller.start <= bigger.end &&
            smaller.end >= bigger.start && smaller.end <= bigger.end
    }

    companion object {
        fun orderSpansBySize(span1: SpanInfo, span2: SpanInfo): Pair<SpanInfo, SpanInfo> =
            if (span1.size() > span2.size()) {
                span2 to span1
            } else {
                span1 to span2
            }

        fun fromString(serialized: String): SpanInfo {
            val parts = serialized.split(":")
            require(parts.size == 3) { "Invalid serialized format" }

            val start = parts[0].toIntOrNull() ?: error("Invalid start value")
            val end = parts[1].toIntOrNull() ?: error("Invalid end value")
            val span = Span.textFromString(parts[2])

            return SpanInfo(start, end, span)
        }
    }
}

enum class Interception {
    INSIDE,
    INTERSECT,
    OUTSIDE,
    CONTAINING
}

enum class Span(val label: String) {
    BOLD("BOLD"),
    ITALIC("ITALIC"),
    UNDERLINE("UNDERLINE"),
    HIGHLIGHT("HIGHLIGHT");

    fun toText() = this.label

    companion object {
        fun textFromString(label: String): Span = entries.find { option ->
            option.label == label
        } ?: throw IllegalArgumentException("This is span can't be found: $label")
    }
}
