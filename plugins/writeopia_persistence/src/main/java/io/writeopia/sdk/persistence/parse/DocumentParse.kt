package io.writeopia.sdk.persistence.parse

import io.writeopia.sdk.models.document.Document
import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.sdk.persistence.entity.document.DocumentEntity

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
