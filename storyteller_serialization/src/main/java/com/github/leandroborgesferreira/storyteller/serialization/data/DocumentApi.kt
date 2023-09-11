package com.github.leandroborgesferreira.storyteller.serialization.data

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.Instant
import java.util.UUID

@Serializable
data class DocumentApi(
    val id: String = "",
    val title: String = "",
    val content: List<StoryStepApi> = emptyList(),
    @Contextual val createdAt: Instant = Instant.now(),
    @Contextual val lastUpdatedAt: Instant = Instant.now(),
    val userId: String,
)
