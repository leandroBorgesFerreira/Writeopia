package io.writeopia.sdk.models.document

import io.writeopia.sdk.models.story.StoryStep
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

data class Document(
    val id: String,
    val title: String = "",
    val content: Map<Int, StoryStep> = emptyMap(),
    val createdAt: Instant,
    val lastUpdatedAt: Instant,
    val userId: String,
)
