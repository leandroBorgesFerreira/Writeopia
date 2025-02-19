package io.writeopia.manager

import io.writeopia.sdk.manager.SpansHandler
import io.writeopia.sdk.models.span.Span
import io.writeopia.sdk.models.span.SpanInfo
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SpansHandlerTest {

    @Test
    fun `it should be possible to add a span`() {
        val boldSpan = SpanInfo.create(start = 0, end = 5, Span.BOLD)
        val otherBoldSpan = SpanInfo.create(start = 7, end = 10, Span.BOLD)
        val newSpans = SpansHandler.toggleSpans(setOf(boldSpan), otherBoldSpan)

        assertTrue { newSpans.contains(otherBoldSpan) }
    }

    @Test
    fun `spans should be ordered before insertion`() {
        val boldSpan = SpanInfo.create(start = 0, end = 5, Span.BOLD)
        val otherBoldSpan = SpanInfo.create(start = 10, end = 7, Span.BOLD)
        val expected = SpanInfo.create(start = 7, end = 10, Span.BOLD)

        val newSpans = SpansHandler.toggleSpans(setOf(boldSpan), otherBoldSpan)

        assertTrue { newSpans.contains(expected) }
    }

    @Test
    fun `it should be possible to remove a span`() {
        val boldSpan = SpanInfo.create(start = 0, end = 5, Span.BOLD)
        val newSpans = SpansHandler.toggleSpans(setOf(boldSpan), boldSpan)

        assertFalse { newSpans.contains(boldSpan) }
    }

    @Test
    fun `it should be possible to expand a span`() {
        val boldSpan = SpanInfo.create(start = 0, end = 5, Span.BOLD)
        val boldSpan1 = SpanInfo.create(start = 3, end = 10, Span.BOLD)

        val expected = SpanInfo.create(start = 0, end = 10, Span.BOLD)
        val newSpans = SpansHandler.toggleSpans(setOf(boldSpan), boldSpan1)

        assertEquals(setOf(expected), newSpans)
    }

    @Test
    fun `it should be possible to expand a span with inverted selection`() {
        val boldSpan = SpanInfo.create(start = 5, end = 0, Span.BOLD)
        val boldSpan1 = SpanInfo.create(start = 10, end = 3, Span.BOLD)

        val expected = SpanInfo.create(start = 0, end = 10, Span.BOLD)
        val newSpans = SpansHandler.toggleSpans(setOf(boldSpan), boldSpan1)

        assertEquals(setOf(expected), newSpans)
    }

    @Test
    fun `adding a span inside another span should split it`() {
        val boldSpan = SpanInfo.create(start = 0, end = 10, Span.BOLD)
        val boldSpan1 = SpanInfo.create(start = 2, end = 8, Span.BOLD)

        val expected = setOf(
            SpanInfo.create(start = 0, end = 2, Span.BOLD),
            SpanInfo.create(start = 8, end = 10, Span.BOLD)
        )

        val newSpans = SpansHandler.toggleSpans(setOf(boldSpan), boldSpan1)

        assertEquals(expected, newSpans)
    }

    @Test
    fun `when adding different spans they should live together`() {
        val boldSpan = SpanInfo.create(start = 0, end = 5, Span.BOLD)
        val italicSpan = SpanInfo.create(start = 0, end = 5, Span.ITALIC)

        val expected = setOf(boldSpan, italicSpan)
        val newSpans = SpansHandler.toggleSpans(setOf(boldSpan), italicSpan)

        assertEquals(expected, newSpans)
    }

    @Test
    fun `when adding a containing span only the new one should live`() {
        val boldSpan = SpanInfo.create(start = 2, end = 10, Span.BOLD)
        val boldSpan1 = SpanInfo.create(start = 0, end = 15, Span.BOLD)

        val expected = setOf(boldSpan1)

        val newSpans = SpansHandler.toggleSpans(setOf(boldSpan), boldSpan1)

        assertEquals(expected, newSpans)
    }

    @Test
    fun `it should be possible to crop a span from end`() {
        val boldSpan = SpanInfo.create(start = 0, end = 10, Span.BOLD)
        val boldSpan1 = SpanInfo.create(start = 5, end = 10, Span.BOLD)

        val expected = setOf(boldSpan.copy(end = 5))
        val newSpans = SpansHandler.toggleSpans(setOf(boldSpan), boldSpan1)

        assertEquals(expected, newSpans)
    }

    @Test
    fun `it should be possible to crop a span from start`() {
        val boldSpan = SpanInfo.create(start = 0, end = 10, Span.BOLD)
        val boldSpan1 = SpanInfo.create(start = 0, end = 5, Span.BOLD)

        val expected = setOf(boldSpan.copy(start = 5))
        val newSpans = SpansHandler.toggleSpans(setOf(boldSpan), boldSpan1)

        assertEquals(expected, newSpans)
    }
}
