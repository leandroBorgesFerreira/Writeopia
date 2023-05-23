package com.github.leandroborgesferreira.storyteller.persistence.parse

import com.github.leandroborgesferreira.storyteller.model.document.Document
import com.github.leandroborgesferreira.storyteller.model.story.StoryUnit
import com.github.leandroborgesferreira.storyteller.persistence.entity.document.DocumentEntity

fun DocumentEntity.toModel(content: Map<Int, StoryUnit> = emptyMap()) = Document(
    id = id,
    title = title,
    content = content,
)

fun Document.toEntity() = DocumentEntity(
    id = id,
    title = title,
)
