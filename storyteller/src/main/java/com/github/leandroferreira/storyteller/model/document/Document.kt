package com.github.leandroferreira.storyteller.model.document

import com.github.leandroferreira.storyteller.model.story.StoryUnit

data class Document(
    val id: String,
    val title: String,
    val content: Map<Int, StoryUnit>?,
)
