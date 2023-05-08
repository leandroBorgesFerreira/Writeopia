package br.com.leandroferreira.storyteller.model.document

import br.com.leandroferreira.storyteller.model.story.StoryUnit

data class Document(
    val id: String,
    val title: String,
    val content: Map<Int, StoryUnit>,
)
