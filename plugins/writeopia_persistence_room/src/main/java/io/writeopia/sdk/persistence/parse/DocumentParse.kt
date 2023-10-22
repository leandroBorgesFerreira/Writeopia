package io.writeopia.sdk.persistence.parse

import io.writeopia.sdk.models.document.Document
import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.sdk.persistence.entity.document.DocumentEntity
import kotlinx.datetime.Instant

fun DocumentEntity.toModel(content: Map<Int, StoryStep> = emptyMap()) = Document(
    id = id,
    title = title,
    content = content,
    createdAt = Instant.fromEpochMilliseconds(createdAt),
    lastUpdatedAt = Instant.fromEpochMilliseconds(lastUpdatedAt),
    userId = userId
)

fun Document.toEntity() = DocumentEntity(
    id = id,
    title = title,
    createdAt = createdAt.toEpochMilliseconds(),
    lastUpdatedAt = lastUpdatedAt.toEpochMilliseconds(),
    userId = userId,
)
