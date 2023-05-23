package com.github.leandroferreira.storyteller.persistence.parse

import com.github.leandroferreira.storyteller.model.document.Document
import com.github.leandroferreira.storyteller.model.story.StoryUnit
import com.github.leandroferreira.storyteller.persistence.entity.document.DocumentEntity

fun DocumentEntity.toModel(content: Map<Int, StoryUnit> = emptyMap()) = Document(
    id = id,
    title = title,
    content = content,
)

fun Document.toEntity() = DocumentEntity(
    id = id,
    title = title,
)
