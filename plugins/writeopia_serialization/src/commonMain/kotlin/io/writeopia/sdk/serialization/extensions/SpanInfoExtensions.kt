package io.writeopia.sdk.serialization.extensions

import io.writeopia.sdk.models.span.Span
import io.writeopia.sdk.models.span.SpanInfo
import io.writeopia.sdk.serialization.data.SpanInfoApi

fun SpanInfoApi.toModel() = SpanInfo.create(
    start = this.start,
    end = this.end,
    span = Span.textFromString(this.span),
)

fun SpanInfo.toApi() = SpanInfoApi(
    start = this.start,
    end = this.end,
    span = this.span.label,
)
