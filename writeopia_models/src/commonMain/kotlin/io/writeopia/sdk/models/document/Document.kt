package io.writeopia.sdk.models.document

import io.writeopia.sdk.models.story.StoryStep
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import java.util.UUID

data class Document(
    val id: String = UUID.randomUUID().toString(),
    val title: String = "",
    val content: Map<Int, StoryStep> = emptyMap(),
    val createdAt: Instant,
    val lastUpdatedAt: Instant,
    val userId: String,
)
