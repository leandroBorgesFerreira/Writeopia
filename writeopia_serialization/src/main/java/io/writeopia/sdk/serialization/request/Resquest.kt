package io.writeopia.sdk.serialization.request

import kotlinx.serialization.Serializable

@Serializable
data class WriteopiaRequest<T>(val data: T)

fun <T> T.wrapInRequest() = WriteopiaRequest(this)
