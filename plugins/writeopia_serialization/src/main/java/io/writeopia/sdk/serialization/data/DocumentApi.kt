package io.writeopia.sdk.serialization.data

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class DocumentApi(
    val id: String = "",
    val title: String = "",
    val content: List<StoryStepApi> = emptyList(),
    @Contextual val createdAt: Instant = Clock.System.now(),
    @Contextual val lastUpdatedAt: Instant = Clock.System.now(),
    val userId: String,
)
