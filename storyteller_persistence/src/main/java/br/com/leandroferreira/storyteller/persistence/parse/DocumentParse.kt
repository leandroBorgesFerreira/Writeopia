package br.com.leandroferreira.storyteller.persistence.parse

import br.com.leandroferreira.storyteller.model.document.Document
import br.com.leandroferreira.storyteller.model.story.StoryUnit
import br.com.leandroferreira.storyteller.persistence.entity.document.DocumentEntity

fun DocumentEntity.toModel(content: Map<Int, StoryUnit> = emptyMap()) = Document(
    id = id,
    title = title,
    content = content,
)

fun Document.toEntity() = DocumentEntity(
    id = id,
    title = title,
)
