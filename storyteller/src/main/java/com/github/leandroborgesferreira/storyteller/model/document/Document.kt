package com.github.leandroborgesferreira.storyteller.model.document

import com.github.leandroborgesferreira.storyteller.model.story.StoryStep

data class Document(
    val id: String,
    val title: String,
    val content: Map<Int, StoryStep>?,
)