package io.writeopia.sdk.persistence.sqldelight.mapper

import io.writeopia.sdk.models.document.Document
import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.sql.DocumentEntity
import kotlinx.datetime.Instant

fun DocumentEntity.toModel(content: Map<Int, StoryStep> = emptyMap()) = Document(
    id = id,
    title = title,
    content = content,
    createdAt = Instant.fromEpochMilliseconds(created_at),
    lastUpdatedAt = Instant.fromEpochMilliseconds(last_updated_at),
    userId = user_id,
)