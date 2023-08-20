package com.github.leandroborgesferreira.storyteller.model.document

import com.github.leandroborgesferreira.storyteller.models.story.StoryStep
import java.time.Instant
import java.util.UUID

data class Document(
    val id: String = UUID.randomUUID().toString(),
    val title: String = "",
    val content: Map<Int, StoryStep>? = emptyMap(),
    val createdAt: Instant = Instant.now(),
    val lastUpdatedAt: Instant = Instant.now(),
)
