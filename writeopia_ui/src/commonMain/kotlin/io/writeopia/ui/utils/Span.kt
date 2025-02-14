package io.writeopia.ui.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import io.writeopia.sdk.models.span.SpanInfo
import io.writeopia.ui.extensions.toSpanStyle
import kotlin.math.min

object Spans {
    fun createStringWithSpans(text: String?, spans: Iterable<SpanInfo>): AnnotatedString {
        val lastPosition = text?.length ?: 0

        return buildAnnotatedString {
            append(text.takeIf { it?.isNotEmpty() == true } ?: "")

            spans.forEach { spanInfo ->
                addStyle(
                    spanInfo.span.toSpanStyle(),
                    min(lastPosition, spanInfo.start),
                    min(lastPosition, spanInfo.end)
                )
            }
        }
    }

}
