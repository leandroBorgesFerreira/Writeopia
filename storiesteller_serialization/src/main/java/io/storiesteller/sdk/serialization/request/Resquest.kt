package io.storiesteller.sdk.serialization.request

import kotlinx.serialization.Serializable

@Serializable
data class StoriesTellerRequest<T>(val data: T)

fun <T> T.wrapInRequest() = StoriesTellerRequest(this)
