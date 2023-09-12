package com.storiesteller.sdk.serialization.request

import kotlinx.serialization.Serializable

@Serializable
data class StoryTellerRequest<T>(val data: T)

fun <T> T.wrapInRequest() = StoryTellerRequest(this)
