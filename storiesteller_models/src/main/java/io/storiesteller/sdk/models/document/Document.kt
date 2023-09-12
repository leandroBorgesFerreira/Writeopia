package io.storiesteller.sdk.models.document

import io.storiesteller.sdk.models.story.StoryStep
import java.time.Instant
import java.util.UUID

data class Document(
    val id: String = UUID.randomUUID().toString(),
    val title: String = "",
    val content: Map<Int, StoryStep> = emptyMap(),
    val createdAt: Instant = Instant.now(),
    val lastUpdatedAt: Instant = Instant.now(),
    val userId: String,
)
