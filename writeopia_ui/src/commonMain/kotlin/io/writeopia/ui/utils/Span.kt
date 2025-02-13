package io.writeopia.ui.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import io.writeopia.sdk.models.span.SpanInfo
import io.writeopia.ui.extensions.toSpanStyle

object Spans {
    fun createStringWithSpans(text: String?, spans: Iterable<SpanInfo>): AnnotatedString =
        buildAnnotatedString {
            append(text.takeIf { it?.isNotEmpty() == true } ?: "")

            spans.forEach { spanInfo ->
                addStyle(
                    spanInfo.span.toSpanStyle(),
                    spanInfo.start,
                    spanInfo.end
                )
            }
        }
}
