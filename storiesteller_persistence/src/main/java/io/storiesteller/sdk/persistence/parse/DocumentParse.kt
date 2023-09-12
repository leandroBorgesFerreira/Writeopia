package io.storiesteller.sdk.persistence.parse

import io.storiesteller.sdk.models.document.Document
import io.storiesteller.sdk.models.story.StoryStep
import io.storiesteller.sdk.persistence.entity.document.DocumentEntity

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
