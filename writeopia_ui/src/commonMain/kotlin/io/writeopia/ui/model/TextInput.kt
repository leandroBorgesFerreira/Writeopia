package io.writeopia.ui.model

import io.writeopia.sdk.models.span.SpanInfo

data class TextInput(val text: String, val start: Int, val end: Int, val spans: Set<SpanInfo>)
