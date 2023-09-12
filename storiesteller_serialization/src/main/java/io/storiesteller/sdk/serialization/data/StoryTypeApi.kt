package io.storiesteller.sdk.serialization.data

import kotlinx.serialization.Serializable

@Serializable
data class StoryTypeApi(val name: String, val number: Int)