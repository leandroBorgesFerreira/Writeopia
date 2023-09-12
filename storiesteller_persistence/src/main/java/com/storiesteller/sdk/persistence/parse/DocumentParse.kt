package com.storiesteller.sdk.persistence.parse

import com.storiesteller.sdk.models.document.Document
import com.storiesteller.sdk.models.story.StoryStep
import com.storiesteller.sdk.persistence.entity.document.DocumentEntity

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
