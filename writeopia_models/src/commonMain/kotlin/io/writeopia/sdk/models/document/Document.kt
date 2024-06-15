package io.writeopia.sdk.models.document

import io.writeopia.sdk.models.id.GenerateId
import io.writeopia.sdk.models.story.StoryStep
import kotlinx.datetime.Instant

data class Document(
    val id: String = GenerateId.generate(),
    val title: String = "",
    val content: Map<Int, StoryStep> = emptyMap(),
    val createdAt: Instant,
    val lastUpdatedAt: Instant,
    val userId: String,
    val favorite: Boolean = false
)
