package com.github.leandroborgesferreira.storyteller.persistence.parse

import com.github.leandroborgesferreira.storyteller.models.document.Document
import com.github.leandroborgesferreira.storyteller.models.story.StoryStep
import com.github.leandroborgesferreira.storyteller.persistence.entity.document.DocumentEntity

fun DocumentEntity.toModel(content: Map<Int, StoryStep> = emptyMap()) = Document(
    id = id,
    title = title,
    content = content,
    createdAt = createdAt,
    lastUpdatedAt = lastUpdatedAt,
    userId = userId
)

fun Document.toEntity() = DocumentEntity(
    id = id,
    title = title,
    createdAt = createdAt,
    lastUpdatedAt = lastUpdatedAt,
    userId = userId,
)
