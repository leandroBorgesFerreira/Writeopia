package com.github.leandroborgesferreira.storyteller.model.document

import com.github.leandroborgesferreira.storyteller.models.story.StoryStep
import java.util.Date
import java.util.UUID

data class Document(
    val id: String = UUID.randomUUID().toString(),
    val title: String = "",
    val content: Map<Int, StoryStep>? = emptyMap(),
    val createdAt: Date = Date(),
    val lastUpdatedAt: Date = Date(),
)
